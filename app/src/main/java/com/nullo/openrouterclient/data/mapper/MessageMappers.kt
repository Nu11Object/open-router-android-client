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

fun Query.toDto(): QueryDto = QueryDto(
    role = ROLE_USER,
    content = text
)

fun List<Message>.toDto(): List<QueryDto> = map { message ->
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

fun MessageDbEntity.toMessage(): Message = when (role) {
    ROLE_USER -> {
        Query(id = id, text = text)
    }

    ROLE_ASSISTANT -> {
        when {
            isLoading -> {
                Loading(id = id, text = text)
            }

            error != null -> {
                Error(id = id, text = text, header = error)
            }

            else -> {
                AiResponse(id = id, text = text, reasoning = reasoning)
            }
        }
    }

    else -> throw IllegalArgumentException(
        "Unknown message database entity $this."
    )
}
