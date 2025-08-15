package com.nullo.openrouterclient.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.nullo.openrouterclient.data.database.aiModels.AiModelsProvider
import com.nullo.openrouterclient.data.mapper.AiModelMapper
import com.nullo.openrouterclient.di.qualifiers.ApiKeyQualifier
import com.nullo.openrouterclient.di.qualifiers.SettingsQualifier
import com.nullo.openrouterclient.domain.entities.AiModel
import com.nullo.openrouterclient.domain.repositories.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val aiModelMapper: AiModelMapper,
    private val aiModelsProvider: AiModelsProvider,
    @param:SettingsQualifier private val settingsPrefs: SharedPreferences,
    @param:ApiKeyQualifier private val apiKeyPrefs: SharedPreferences,
    private val gson: Gson,
) : SettingsRepository {

    private val _currentModel = MutableStateFlow(getSavedAiModelOrDefault())
    override val currentModel = _currentModel.asStateFlow()

    private val _contextEnabled = MutableStateFlow(getContextMode())
    override val contextEnabled = _contextEnabled.asStateFlow()

    private val _apiKey = MutableStateFlow(getApiKey())
    override val apiKey = _apiKey.asStateFlow()

    private fun getSavedAiModelOrDefault(): AiModel {
        val modelAsString = settingsPrefs.getString(KEY_AI_MODEL, null)
        return runCatching {
            gson.fromJson(modelAsString, AiModel::class.java)
        }.getOrNull() ?: getDefaultModel()
    }

    private fun getContextMode(): Boolean {
        return settingsPrefs.getBoolean(KEY_CONTEXT_ENABLED, false)
    }

    private fun getApiKey(): String {
        return apiKeyPrefs.getString(KEY_API_KEY, API_KEY_EMPTY) ?: API_KEY_EMPTY
    }

    private fun getDefaultModel(): AiModel {
        return aiModelMapper.mapDbEntityToAiModel(aiModelsProvider.getDefaultModel())
    }

    override fun selectAiModel(aiModel: AiModel) {
        _currentModel.value = aiModel
        saveModel(aiModel)
    }

    override fun toggleContextMode() {
        val newValue = !_contextEnabled.value
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
