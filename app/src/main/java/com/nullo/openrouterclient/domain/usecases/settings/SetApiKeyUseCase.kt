package com.nullo.openrouterclient.domain.usecases.settings

import com.nullo.openrouterclient.domain.repositories.SettingsRepository
import javax.inject.Inject

class SetApiKeyUseCase @Inject constructor(
    private val repository: SettingsRepository
) {

    operator fun invoke(apiKey: String) {
        repository.setApiKey(apiKey)
    }
}
