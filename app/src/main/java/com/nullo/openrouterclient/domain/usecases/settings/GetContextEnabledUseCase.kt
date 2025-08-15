package com.nullo.openrouterclient.domain.usecases.settings

import com.nullo.openrouterclient.domain.repositories.SettingsRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetContextEnabledUseCase @Inject constructor(
    private val repository: SettingsRepository
) {

    operator fun invoke(): StateFlow<Boolean> {
        return repository.contextEnabled
    }
}
