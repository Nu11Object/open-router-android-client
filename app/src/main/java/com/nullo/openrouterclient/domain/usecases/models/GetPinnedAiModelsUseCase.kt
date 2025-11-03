package com.nullo.openrouterclient.domain.usecases.models

import com.nullo.openrouterclient.domain.entities.AiModel
import com.nullo.openrouterclient.domain.repositories.AiModelsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPinnedAiModelsUseCase @Inject constructor(
    private val repository: AiModelsRepository
) {

    operator fun invoke(): Flow<List<AiModel>> {
        return repository.pinnedAiModels
    }
}
