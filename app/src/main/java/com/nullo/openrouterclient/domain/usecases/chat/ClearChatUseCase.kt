package com.nullo.openrouterclient.domain.usecases.chat

import com.nullo.openrouterclient.domain.repositories.ChatRepository
import javax.inject.Inject

class ClearChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {

    suspend operator fun invoke() {
        repository.clearMessages()
    }
}
