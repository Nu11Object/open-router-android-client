package com.nullo.openrouterclient.data.repository

import com.nullo.openrouterclient.data.database.aiModels.AiModelsDao
import com.nullo.openrouterclient.data.mapper.toAiModel
import com.nullo.openrouterclient.data.mapper.toAiModels
import com.nullo.openrouterclient.data.mapper.toDbEntity
import com.nullo.openrouterclient.data.network.ApiService
import com.nullo.openrouterclient.domain.entities.AiModel
import com.nullo.openrouterclient.domain.repositories.AiModelsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AiModelsRepositoryImpl @Inject constructor(
    private val aiModelsDao: AiModelsDao,
    private val apiService: ApiService,
) : AiModelsRepository {

    override val pinnedAiModels: Flow<List<AiModel>> = aiModelsDao
        .getModels()
        .map { dbModels -> dbModels.map { it.toAiModel() } }

    override suspend fun getCloudAiModels(): List<AiModel> {
        val response = apiService.getModels()
        return response.takeIf { it.isSuccessful }
            ?.body()
            ?.toAiModels()
            ?: emptyList()
    }

    override suspend fun pinAiModel(aiModel: AiModel) {
        val model = aiModel.toDbEntity()
        aiModelsDao.insertModel(model)
    }

    override suspend fun unpinAiModel(aiModel: AiModel) {
        val model = aiModel.toDbEntity()
        aiModelsDao.removeModel(model)
    }
}
