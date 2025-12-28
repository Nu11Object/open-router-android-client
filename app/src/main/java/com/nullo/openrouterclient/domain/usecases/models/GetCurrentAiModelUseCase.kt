package com.nullo.openrouterclient.domain.usecases.models

import com.nullo.openrouterclient.domain.entities.AiModel
import com.nullo.openrouterclient.domain.repositories.SettingsRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetCurrentAiModelUseCase @Inject constructor(
    private val repository: SettingsRepository
) {

    operator fun invoke(): StateFlow<AiModel?> {
        return repository.currentModel
    }
}
