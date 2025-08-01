package com.nullo.openrouterclient.domain.usecases.settings

import androidx.lifecycle.LiveData
import com.nullo.openrouterclient.domain.repositories.SettingsRepository
import javax.inject.Inject

class GetApiKeyUseCase @Inject constructor(
    private val repository: SettingsRepository
) {

    operator fun invoke(): LiveData<String> {
        return repository.apiKey
    }
}
