package com.nullo.openrouterclient.domain.repositories

import androidx.lifecycle.LiveData
import com.nullo.openrouterclient.domain.entities.AiModel

interface AiModelsRepository {

    fun getPinnedAiModels(): LiveData<List<AiModel>>

    suspend fun getCloudAiModels(): List<AiModel>

    suspend fun pinAiModel(aiModel: AiModel)

    suspend fun unpinAiModel(aiModel: AiModel)
}
