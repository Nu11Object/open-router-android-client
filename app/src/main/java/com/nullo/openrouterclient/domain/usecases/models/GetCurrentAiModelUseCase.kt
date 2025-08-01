package com.nullo.openrouterclient.domain.usecases.models

import androidx.lifecycle.LiveData
import com.nullo.openrouterclient.domain.entities.AiModel
import com.nullo.openrouterclient.domain.repositories.SettingsRepository
import javax.inject.Inject

class GetCurrentAiModelUseCase @Inject constructor(
    private val repository: SettingsRepository
) {

    operator fun invoke(): LiveData<AiModel> {
        return repository.currentModel
    }
}
