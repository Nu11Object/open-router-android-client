package com.nullo.openrouterclient.domain.usecases.chat

import com.nullo.openrouterclient.domain.entities.Message
import com.nullo.openrouterclient.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {

    operator fun invoke(): Flow<List<Message>> {
        return repository.messages
    }
}
