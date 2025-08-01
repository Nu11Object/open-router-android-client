package com.nullo.openrouterclient.domain.usecases.models

import androidx.lifecycle.LiveData
import com.nullo.openrouterclient.domain.entities.AiModel
import com.nullo.openrouterclient.domain.repositories.AiModelsRepository
import javax.inject.Inject

class GetPinnedAiModelsUseCase @Inject constructor(
    private val repository: AiModelsRepository
) {

    operator fun invoke(): LiveData<List<AiModel>> {
        return repository.getPinnedAiModels()
    }
}
