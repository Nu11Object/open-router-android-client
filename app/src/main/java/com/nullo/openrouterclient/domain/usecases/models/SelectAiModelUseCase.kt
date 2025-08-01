package com.nullo.openrouterclient.domain.usecases.models

import com.nullo.openrouterclient.domain.entities.AiModel
import com.nullo.openrouterclient.domain.repositories.SettingsRepository
import javax.inject.Inject

class SelectAiModelUseCase @Inject constructor(
    private val repository: SettingsRepository
) {

    operator fun invoke(aiModel: AiModel) {
        repository.selectAiModel(aiModel)
    }
}
