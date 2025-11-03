package com.nullo.openrouterclient.domain.usecases.settings

import com.nullo.openrouterclient.domain.repositories.SettingsRepository
import javax.inject.Inject

class SetApiKeyUseCase @Inject constructor(
    private val repository: SettingsRepository
) {

    operator fun invoke(apiKey: String) {
        val key = apiKey.takeIf { it.startsWith(PREFIX_BEARER) } ?: (PREFIX_BEARER + apiKey)
        repository.setApiKey(key)
    }

    companion object {
        private const val PREFIX_BEARER = "Bearer "
    }
}
