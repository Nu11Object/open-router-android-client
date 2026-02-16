package com.nullo.openrouterclient.presentation

import android.util.Log
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
    private val _searchQuery = MutableStateFlow("")
    private val _freeModelsOnly = MutableStateFlow(false)

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
            _uiEvents.emit(UiEvent.ClearInput)

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
            _uiEvents.emit(UiEvent.ShowMessage(R.string.saved))
        }
    }

    fun browseCloudModels() {
        browseCloudAiModelJob?.cancel()
        browseCloudAiModelJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loadingCloudAiModels = true)

            try {
                val models = getCloudAiModelsUseCase()
                _cloudAiModels.value = models
            } catch (_: Exception) {
                emitError(ErrorType.NETWORK_ERROR)
            } finally {
                _uiState.value = _uiState.value.copy(loadingCloudAiModels = false)
            }
        }
    }

    fun filterCloudModelsByName(query: String) {
        searchCloudAiModelJob?.cancel()
        searchCloudAiModelJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            _searchQuery.value = query
        }
    }

    fun setFreeModelsFilter(enabled: Boolean) {
        _freeModelsOnly.value = enabled
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
            launch { observeFilters() }
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

    private suspend fun observeFilters() {
        combine(
            _searchQuery,
            _freeModelsOnly,
            _cloudAiModels
        ) { _, _, _ -> }
            .collect {
                applyModelFilters()
            }
    }

    private fun applyModelFilters() {
        val original = _cloudAiModels.value
        val query = _searchQuery.value
        val freeOnly = _freeModelsOnly.value

        val filtered = original.filter { model ->

            val matchesName = if (query.isBlank()) {
                true
            } else {
                model.name.contains(query, ignoreCase = true)
            }

            val matchesFree = if (freeOnly) {
                model.freeToUse
            } else {
                true
            }

            matchesName && matchesFree
        }

        _uiState.value = _uiState.value.copy(cloudAiModels = filtered)
    }

    private suspend fun emitError(errorType: ErrorType) {
        _uiEvents.emit(UiEvent.ShowError(errorType))
    }

    companion object {

        const val TAG_ERROR = "error"
        private const val SEARCH_DEBOUNCE_MS = 300L
    }
}
