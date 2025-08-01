package com.nullo.openrouterclient.domain.usecases.chat

import androidx.lifecycle.LiveData
import com.nullo.openrouterclient.domain.repositories.ChatRepository
import com.nullo.openrouterclient.domain.entities.Message
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {

    operator fun invoke(): LiveData<List<Message>> {
        return repository.getMessages()
    }
}
