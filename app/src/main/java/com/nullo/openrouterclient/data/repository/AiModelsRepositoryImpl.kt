package com.nullo.openrouterclient.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.nullo.openrouterclient.data.database.aiModels.AiModelsDao
import com.nullo.openrouterclient.data.mapper.AiModelMapper
import com.nullo.openrouterclient.data.mapper.ApiResponseMapper
import com.nullo.openrouterclient.data.network.ApiService
import com.nullo.openrouterclient.domain.entities.AiModel
import com.nullo.openrouterclient.domain.repositories.AiModelsRepository
import javax.inject.Inject

class AiModelsRepositoryImpl @Inject constructor(
    private val aiModelsDao: AiModelsDao,
    private val apiService: ApiService,
    private val aiModelMapper: AiModelMapper,
    private val apiResponseMapper: ApiResponseMapper,
) : AiModelsRepository {

    override fun getPinnedAiModels(): LiveData<List<AiModel>> {
        return aiModelsDao.getModels().map {
            it.map { model ->
                aiModelMapper.mapDbEntityToAiModel(model)
            }
        }
    }

    override suspend fun getCloudAiModels(): List<AiModel> {
        val response = apiService.getModels()
        return if (response.isSuccessful) {
            val aiModelsResponseDto = response.body()
            aiModelsResponseDto?.let {
                apiResponseMapper.mapAiModelsResponseDtoToModelsList(aiModelsResponseDto)
            } ?: emptyList()
        } else {
            emptyList()
        }
    }

    override suspend fun pinAiModel(aiModel: AiModel) {
        val model = aiModelMapper.mapAiModelToDbEntity(aiModel)
        aiModelsDao.insertModel(model)
    }

    override suspend fun unpinAiModel(aiModel: AiModel) {
        val model = aiModelMapper.mapAiModelToDbEntity(aiModel)
        aiModelsDao.removeModel(model)
    }
}
