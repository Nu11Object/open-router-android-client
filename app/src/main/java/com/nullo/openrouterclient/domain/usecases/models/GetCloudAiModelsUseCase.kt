package com.nullo.openrouterclient.domain.usecases.models

import com.nullo.openrouterclient.domain.repositories.AiModelsRepository
import com.nullo.openrouterclient.domain.entities.AiModel
import javax.inject.Inject

class GetCloudAiModelsUseCase @Inject constructor(
    private val repository: AiModelsRepository
) {

    suspend operator fun invoke(): List<AiModel> {
        return repository.getCloudAiModels()
    }
}
