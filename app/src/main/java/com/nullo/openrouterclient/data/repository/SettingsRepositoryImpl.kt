package com.nullo.openrouterclient.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.nullo.openrouterclient.data.database.aiModels.AiModelsDao
import com.nullo.openrouterclient.data.mapper.AiModelMapper
import com.nullo.openrouterclient.di.qualifiers.ApiKeyQualifier
import com.nullo.openrouterclient.di.qualifiers.SettingsQualifier
import com.nullo.openrouterclient.domain.entities.AiModel
import com.nullo.openrouterclient.domain.repositories.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val aiModelsDao: AiModelsDao,
    private val aiModelMapper: AiModelMapper,
    @param:SettingsQualifier private val settingsPrefs: SharedPreferences,
    @param:ApiKeyQualifier private val apiKeyPrefs: SharedPreferences,
    private val gson: Gson,
) : SettingsRepository {

    private val _currentModel = MutableLiveData<AiModel>()
    override val currentModel: LiveData<AiModel>
        get() = _currentModel

    private val _contextEnabled = MutableLiveData<Boolean>()
    override val contextEnabled: LiveData<Boolean>
        get() = _contextEnabled

    private val _apiKey = MutableLiveData<String>()
    override val apiKey: LiveData<String>
        get() = _apiKey

    init {
        loadModel()
        loadContextMode()
        loadApiKey()
    }

    private fun loadModel() {
        val modelAsString = settingsPrefs.getString(KEY_AI_MODEL, null)
        CoroutineScope(Dispatchers.IO).launch {
            _currentModel.postValue(
                modelAsString?.let {
                    gson.fromJson(it, AiModel::class.java) ?: getDefaultModel()
                } ?: getDefaultModel()
            )
        }
    }

    private fun loadContextMode() {
        val contextEnabled = settingsPrefs.getBoolean(KEY_CONTEXT_ENABLED, false)
        _contextEnabled.value = contextEnabled
    }

    private fun loadApiKey() {
        val apiKey = apiKeyPrefs.getString(KEY_API_KEY, API_KEY_EMPTY) ?: API_KEY_EMPTY
        _apiKey.value = apiKey
    }

    private suspend fun getDefaultModel(): AiModel {
        return aiModelMapper.mapDbEntityToAiModel(aiModelsDao.getDefaultViewModel())
    }

    override fun selectAiModel(aiModel: AiModel) {
        _currentModel.value = aiModel
        saveModel(aiModel)
    }

    override fun toggleContextMode() {
        val currentValue = _contextEnabled.value ?: false
        val newValue = !currentValue

        _contextEnabled.value = newValue
        saveContextMode(newValue)
    }

    override fun setApiKey(apiKey: String) {
        _apiKey.value = apiKey
        saveApiKey(apiKey)
    }

    private fun saveModel(aiModel: AiModel) {
        settingsPrefs.edit {
            putString(KEY_AI_MODEL, gson.toJson(aiModel))
        }
    }

    private fun saveContextMode(enabled: Boolean) {
        settingsPrefs.edit {
            putBoolean(KEY_CONTEXT_ENABLED, enabled)
        }
    }

    private fun saveApiKey(apiKey: String) {
        apiKeyPrefs.edit {
            putString(KEY_API_KEY, apiKey)
        }
    }

    companion object {

        const val KEY_AI_MODEL = "ai_model"
        const val KEY_API_KEY = "api_key"
        const val KEY_CONTEXT_ENABLED = "context_enabled"
        const val API_KEY_EMPTY = ""
    }
}
