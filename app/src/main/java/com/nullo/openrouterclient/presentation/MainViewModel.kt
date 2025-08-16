package com.nullo.openrouterclient.presentation

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullo.openrouterclient.R
import com.nullo.openrouterclient.domain.entities.AiModel
import com.nullo.openrouterclient.domain.usecases.chat.ClearChatUseCase
import com.nullo.openrouterclient.domain.usecases.chat.GetChatMessagesUseCase
import com.nullo.openrouterclient.domain.usecases.chat.HandleLoadingFailureUseCase
import com.nullo.openrouterclient.domain.usecases.chat.SendQueryUseCase
import com.nullo.openrouterclient.domain.usecases.models.GetCloudAiModelsUseCase
import com.nullo.openrouterclient.domain.usecases.models.GetCurrentAiModelUseCase
import com.nullo.openrouterclient.domain.usecases.models.GetPinnedAiModelsUseCase
import com.nullo.openrouterclient.domain.usecases.models.PinAiModelUseCase
import com.nullo.openrouterclient.domain.usecases.models.SelectAiModelUseCase
import com.nullo.openrouterclient.domain.usecases.models.UnpinAiModelUseCase
import com.nullo.openrouterclient.domain.usecases.settings.GetApiKeyUseCase
import com.nullo.openrouterclient.domain.usecases.settings.GetContextEnabledUseCase
import com.nullo.openrouterclient.domain.usecases.settings.SetApiKeyUseCase
import com.nullo.openrouterclient.domain.usecases.settings.ToggleContextModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val handleLoadingFailureUseCase: HandleLoadingFailureUseCase,
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val getPinnedAiModelsUseCase: GetPinnedAiModelsUseCase,
    private val getCloudAiModelsUseCase: GetCloudAiModelsUseCase,
    private val sendQueryUseCase: SendQueryUseCase,
    private val clearChatUseCase: ClearChatUseCase,
    private val getCurrentAiModelUseCase: GetCurrentAiModelUseCase,
    private val getContextEnabledUseCase: GetContextEnabledUseCase,
    private val getApiKeyUseCase: GetApiKeyUseCase,
    private val selectAiModelUseCase: SelectAiModelUseCase,
    private val toggleContextModeUseCase: ToggleContextModeUseCase,
    private val setApiKeyUseCase: SetApiKeyUseCase,
    private val pinAiModelUseCase: PinAiModelUseCase,
    private val unpinAiModelUseCase: UnpinAiModelUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    private val _cloudAiModels = MutableStateFlow<List<AiModel>>(emptyList())

    private var browseCloudAiModelJob: Job? = null
    private var searchCloudAiModelJob: Job? = null

    init {
        initializeViewModel()
    }

    fun sendQuery(queryText: String) {
        viewModelScope.launch {

            if (_uiState.value.apiKey.isBlank()) {
                emitError(ErrorType.NO_API_KEY)
                return@launch
            }
            if (queryText.isBlank()) {
                emitError(ErrorType.BLANK_INPUT)
                return@launch
            }
            val currentAiModel = _uiState.value.currentAiModel ?: run {
                emitError(ErrorType.MISSING_MODEL)
                return@launch
            }

            _uiState.value = _uiState.value.copy(waitingForResponse = true)

            val context = if (_uiState.value.contextEnabled) _uiState.value.messages else null
            try {
                sendQueryUseCase(
                    model = currentAiModel,
                    queryText = queryText,
                    context = context,
                    apiKey = _uiState.value.apiKey
                )
            } catch (e: Exception) {
                Log.e(TAG_ERROR, "Error in sendQuery", e)
                emitError(ErrorType.NETWORK_ERROR)
            } finally {
                _uiState.value = _uiState.value.copy(waitingForResponse = false)
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            clearChatUseCase()
        }
    }

    fun selectModel(aiModel: AiModel) {
        selectAiModelUseCase(aiModel)
    }

    fun toggleContextEnabled() {
        toggleContextModeUseCase()
    }

    fun setApiKey(apiKey: String) {
        viewModelScope.launch {
            if (apiKey.isBlank()) {
                emitError(ErrorType.BLANK_API_KEY)
                return@launch
            }
            setApiKeyUseCase(apiKey)
            emitUiMessage(R.string.saved)
        }
    }

    fun browseCloudModels() {
        browseCloudAiModelJob?.cancel()
        browseCloudAiModelJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loadingCloudAiModels = true)

            try {
                val models = getCloudAiModelsUseCase()
                _cloudAiModels.value = models
                _uiState.value = _uiState.value.copy(
                    cloudAiModels = models,
                    loadingCloudAiModels = false
                )
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(loadingCloudAiModels = false)
                emitError(ErrorType.NETWORK_ERROR)
            }
        }
    }

    fun filterCloudModelsByName(query: String) {
        searchCloudAiModelJob?.cancel()
        searchCloudAiModelJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            val original = _cloudAiModels.value
            val filtered = if (query.isBlank()) {
                original
            } else {
                original.filter { it.name.contains(query, ignoreCase = true) }
            }
            _uiState.value = _uiState.value.copy(cloudAiModels = filtered)
        }
    }

    fun pinModel(aiModel: AiModel) {
        viewModelScope.launch {
            pinAiModelUseCase(aiModel)
        }
    }

    fun unpinModel(aiModel: AiModel) {
        viewModelScope.launch {
            unpinAiModelUseCase(aiModel)
        }
    }

    private fun initializeViewModel() {
        viewModelScope.launch {
            launch { handleLoadingFailureUseCase() }
            launch { collectUiState() }
        }
    }

    private suspend fun collectUiState() {
        combine(
            getChatMessagesUseCase(),
            getPinnedAiModelsUseCase(),
            getCurrentAiModelUseCase(),
            getContextEnabledUseCase(),
            getApiKeyUseCase(),
        ) { messages, pinnedAiModels, currentAiModel, contextEnabled, apiKey ->
            _uiState.value.copy(
                messages = messages,
                pinnedAiModels = pinnedAiModels,
                currentAiModel = currentAiModel,
                contextEnabled = contextEnabled,
                apiKey = apiKey
            )
        }.collect { newState ->
            _uiState.value = newState
        }
    }

    private suspend fun emitError(errorType: ErrorType) {
        _uiEvents.emit(UiEvent.ShowError(errorType))
    }

    private suspend fun emitUiMessage(@StringRes messageRes: Int) {
        _uiEvents.emit(UiEvent.ShowMessage(messageRes))
    }

    companion object {

        const val TAG_ERROR = "error"
        private const val SEARCH_DEBOUNCE_MS = 300L
    }
}
