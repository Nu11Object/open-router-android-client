package com.nullo.openrouterclient.data.mapper

import com.nullo.openrouterclient.data.Constants.ROLE_ASSISTANT
import com.nullo.openrouterclient.data.Constants.ROLE_USER
import com.nullo.openrouterclient.data.database.chat.MessageDbEntity
import com.nullo.openrouterclient.data.network.dto.chat.QueryDto
import com.nullo.openrouterclient.domain.entities.Message
import com.nullo.openrouterclient.domain.entities.Message.AiResponse
import com.nullo.openrouterclient.domain.entities.Message.Error
import com.nullo.openrouterclient.domain.entities.Message.Loading
import com.nullo.openrouterclient.domain.entities.Message.Query
import javax.inject.Inject

class MessageMapper @Inject constructor() {

    fun mapQueryToDto(query: Query): QueryDto {
        return QueryDto(
            role = ROLE_USER,
            content = query.text
        )
    }

    fun mapMessagesToDto(messages: List<Message>): List<QueryDto> {
        return messages.map { message ->
            val role = when (message) {
                is Query -> ROLE_USER
                is AiResponse -> ROLE_ASSISTANT
                is Loading -> ROLE_ASSISTANT
                is Error -> ROLE_ASSISTANT
            }
            QueryDto(
                role = role,
                content = message.text
            )
        }
    }

    fun mapDbEntityToMessage(messageDbEntity: MessageDbEntity): Message {
        return when (messageDbEntity.role) {
            ROLE_USER -> {
                Query(
                    id = messageDbEntity.id,
                    text = messageDbEntity.text
                )
            }

            ROLE_ASSISTANT -> {
                when {
                    messageDbEntity.isLoading -> {
                        Loading(
                            id = messageDbEntity.id,
                            text = messageDbEntity.text
                        )
                    }

                    messageDbEntity.error != null -> {
                        Error(
                            messageDbEntity.id,
                            messageDbEntity.text,
                            messageDbEntity.error,
                        )
                    }

                    else -> {
                        AiResponse(
                            id = messageDbEntity.id,
                            text = messageDbEntity.text,
                            reasoning = messageDbEntity.reasoning
                        )
                    }
                }
            }

            else -> throw IllegalArgumentException(
                "Unknown message database entity $messageDbEntity."
            )
        }
    }
}
