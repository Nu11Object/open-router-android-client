package com.nullo.openrouterclient.domain.repositories

import com.nullo.openrouterclient.domain.entities.AiModel
import kotlinx.coroutines.flow.Flow

interface AiModelsRepository {

    val pinnedAiModels: Flow<List<AiModel>>

    suspend fun getCloudAiModels(): List<AiModel>

    suspend fun pinAiModel(aiModel: AiModel)

    suspend fun unpinAiModel(aiModel: AiModel)
}
