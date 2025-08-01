package com.nullo.openrouterclient.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullo.openrouterclient.R
import com.nullo.openrouterclient.domain.entities.AiModel
import com.nullo.openrouterclient.domain.entities.Message
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
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    val messages: LiveData<List<Message>> = getChatMessagesUseCase()

    val pinnedAiModels: LiveData<List<AiModel>> = getPinnedAiModelsUseCase()

    private val _cloudAiModels = MutableLiveData<List<AiModel>>()

    private val _filteredCloudAiModels = MutableLiveData<List<AiModel>>()
    val filteredCloudAiModels: LiveData<List<AiModel>>
        get() = _filteredCloudAiModels

    val currentModel: LiveData<AiModel> = getCurrentAiModelUseCase()
    val contextEnabled: LiveData<Boolean> = getContextEnabledUseCase()
    val apiKey: LiveData<String> = getApiKeyUseCase()

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<ErrorType>()
    val error: LiveData<ErrorType> get() = _error

    private val _messageStringRes = MutableLiveData<Int>()
    val userMessageStringRes: LiveData<Int> get() = _messageStringRes

    init {
        viewModelScope.launch {
            handleLoadingFailureUseCase()
        }
    }

    fun sendQuery(queryText: String) {

        if (apiKey.value.isNullOrBlank()) {
            _error.value = ErrorType.NO_API_KEY
            return
        }

        if (queryText.isBlank()) {
            _error.value = ErrorType.BLANK_INPUT
            return
        }

        _loading.value = true
        viewModelScope.launch {
            try {
                val model = currentModel.value ?: run {
                    _error.postValue(ErrorType.MISSING_MODEL)
                    _loading.postValue(false)
                    return@launch
                }
                val key = apiKey.value ?: run {
                    _error.postValue(ErrorType.NO_API_KEY)
                    _loading.postValue(false)
                    return@launch
                }
                val context = if (contextEnabled.value == true) messages.value else null
                sendQueryUseCase(model, queryText, context, key)
            } catch (e: Exception) {
                Log.e(TAG_ERROR, "Error in sendQuery", e)
                _error.postValue(ErrorType.NETWORK_ERROR)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun clearChat() {
        _loading.value = true
        viewModelScope.launch {
            clearChatUseCase()
            _loading.postValue(false)
        }
    }

    fun selectModel(aiModel: AiModel) {
        selectAiModelUseCase(aiModel)
    }

    fun toggleContextEnabled() {
        toggleContextModeUseCase()
    }

    fun setApiKey(apiKey: String) {
        if (apiKey.isBlank()) {
            _error.value = ErrorType.BLANK_API_KEY
            return
        }
        setApiKeyUseCase(apiKey)
        _messageStringRes.value = R.string.saved
    }

    fun browseCloudModels() {
        viewModelScope.launch {
            val models = getCloudAiModelsUseCase()
            _cloudAiModels.postValue(models)
            _filteredCloudAiModels.postValue(models)
        }
    }

    fun filterCloudModelsByName(query: String) {
        val original = _cloudAiModels.value.orEmpty()
        _filteredCloudAiModels.value = if (query.isBlank()) {
            original
        } else {
            original.filter { it.name.contains(query, ignoreCase = true) }
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

    companion object {

        const val TAG_ERROR = "error"
    }
}
