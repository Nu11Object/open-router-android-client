package com.nullo.openrouterclient.domain.usecases.settings

import com.nullo.openrouterclient.domain.repositories.SettingsRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetApiKeyUseCase @Inject constructor(
    private val repository: SettingsRepository
) {

    operator fun invoke(): StateFlow<String> {
        return repository.apiKey
    }
}
