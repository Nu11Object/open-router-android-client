package com.nullo.openrouterclient.data.repository

import com.nullo.openrouterclient.data.database.aiModels.AiModelsDao
import com.nullo.openrouterclient.data.mapper.AiModelMapper
import com.nullo.openrouterclient.data.mapper.ApiResponseMapper
import com.nullo.openrouterclient.data.network.ApiService
import com.nullo.openrouterclient.di.qualifiers.ApplicationScopeQualifier
import com.nullo.openrouterclient.domain.entities.AiModel
import com.nullo.openrouterclient.domain.repositories.AiModelsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class AiModelsRepositoryImpl @Inject constructor(
    private val aiModelsDao: AiModelsDao,
    private val apiService: ApiService,
    private val aiModelMapper: AiModelMapper,
    private val apiResponseMapper: ApiResponseMapper,
    @param:ApplicationScopeQualifier private val coroutineScope: CoroutineScope,
) : AiModelsRepository {

    override val pinnedAiModels: StateFlow<List<AiModel>> = aiModelsDao.getModels()
        .map { entity ->
            entity.map { aiModelMapper.mapDbEntityToAiModel(it) }
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    override suspend fun getCloudAiModels(): List<AiModel> {
        val response = apiService.getModels()
        return response.takeIf { it.isSuccessful }
            ?.body()
            ?.let { apiResponseMapper.mapAiModelsResponseDtoToModelsList(it) }
            ?: emptyList()
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
