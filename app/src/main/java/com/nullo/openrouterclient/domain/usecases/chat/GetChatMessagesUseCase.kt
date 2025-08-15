package com.nullo.openrouterclient.domain.usecases.chat

import com.nullo.openrouterclient.domain.entities.Message
import com.nullo.openrouterclient.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {

    operator fun invoke(): StateFlow<List<Message>> {
        return repository.messages
    }
}
