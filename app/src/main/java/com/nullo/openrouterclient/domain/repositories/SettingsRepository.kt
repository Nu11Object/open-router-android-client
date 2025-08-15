package com.nullo.openrouterclient.domain.repositories

import com.nullo.openrouterclient.domain.entities.AiModel
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {

    val currentModel: StateFlow<AiModel>

    val contextEnabled: StateFlow<Boolean>

    val apiKey: StateFlow<String>

    fun selectAiModel(aiModel: AiModel)

    fun toggleContextMode()

    fun setApiKey(apiKey: String)
}
