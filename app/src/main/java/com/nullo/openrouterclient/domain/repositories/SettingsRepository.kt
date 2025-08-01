package com.nullo.openrouterclient.domain.repositories

import androidx.lifecycle.LiveData
import com.nullo.openrouterclient.domain.entities.AiModel

interface SettingsRepository {

    val currentModel: LiveData<AiModel>

    val contextEnabled: LiveData<Boolean>

    val apiKey: LiveData<String>

    fun selectAiModel(aiModel: AiModel)

    fun toggleContextMode()

    fun setApiKey(apiKey: String)
}
