package com.nullo.openrouterclient.domain.repositories

import androidx.lifecycle.LiveData
import com.nullo.openrouterclient.domain.entities.AiModel
import com.nullo.openrouterclient.domain.entities.ChatResponseResult
import com.nullo.openrouterclient.domain.entities.Message
import com.nullo.openrouterclient.domain.entities.Message.Query

interface ChatRepository {

    fun getMessages(): LiveData<List<Message>>

    suspend fun addLoadingMessage(): Long

    suspend fun addQueryMessage(queryText: String): Long

    suspend fun clearMessages()

    suspend fun failLoadingMessages()

    suspend fun replaceLoadingWithError(error: ChatResponseResult.Error, loadingMessageId: Long)

    suspend fun replaceLoadingWithNetworkError(loadingMessageId: Long)

    suspend fun replaceLoadingWithResponse(
        responseResult: ChatResponseResult.Success,
        loadingMessageId: Long

    )

    suspend fun sendQuery(
        model: AiModel,
        query: Query,
        context: List<Message>? = null,
        apiKey: String
    ): ChatResponseResult
}
