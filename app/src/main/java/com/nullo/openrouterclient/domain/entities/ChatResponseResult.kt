package com.nullo.openrouterclient.domain.entities

import com.nullo.openrouterclient.domain.entities.Message.AiResponse

sealed class ChatResponseResult {
    data class Success(val response: AiResponse) : ChatResponseResult()
    data class Error(val header: String, val message: String) : ChatResponseResult()
}
