package com.nullo.openrouterclient.data.repository

import com.nullo.openrouterclient.data.ErrorResponseProvider
import com.nullo.openrouterclient.data.database.chat.ChatDao
import com.nullo.openrouterclient.data.database.chat.MessageDbEntityProvider
import com.nullo.openrouterclient.data.mapper.ApiResponseMapper
import com.nullo.openrouterclient.data.mapper.MessageMapper
import com.nullo.openrouterclient.data.network.ApiService
import com.nullo.openrouterclient.data.network.dto.chat.RequestBodyDto
import com.nullo.openrouterclient.domain.entities.AiModel
import com.nullo.openrouterclient.domain.entities.ChatResponseResult
import com.nullo.openrouterclient.domain.entities.Message
import com.nullo.openrouterclient.domain.entities.Message.Query
import com.nullo.openrouterclient.domain.repositories.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao,
    private val apiService: ApiService,
    private val messageDbEntityProvider: MessageDbEntityProvider,
    private val errorResponseProvider: ErrorResponseProvider,
    private val messageMapper: MessageMapper,
    private val apiResponseMapper: ApiResponseMapper,
    private val coroutineScope: CoroutineScope,
) : ChatRepository {

    override val messages: StateFlow<List<Message>> = chatDao.getMessages()
        .map { entityList ->
            entityList.map { messageMapper.mapDbEntityToMessage(it) }
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    override suspend fun addLoadingMessage(): Long {
        val loadingMessage = messageDbEntityProvider.createLoadingMessage()
        val generatedId = chatDao.insertMessage(loadingMessage)
        return generatedId
    }

    override suspend fun addQueryMessage(queryText: String): Long {
        val queryMessage = messageDbEntityProvider.createQueryMessage(queryText)
        val generatedId = chatDao.insertMessage(queryMessage)
        return generatedId
    }

    override suspend fun clearMessages() {
        chatDao.clearAllMessages()
    }

    override suspend fun failLoadingMessages() {
        val error = errorResponseProvider.createCancelledError()
        chatDao.failLoadingMessages(error.header, error.message)
    }

    override suspend fun replaceLoadingWithError(
        error: ChatResponseResult.Error,
        loadingMessageId: Long
    ) {
        chatDao.setErrorIntoMessageById(
            loadingMessageId,
            error.header,
            error.message
        )
    }

    override suspend fun replaceLoadingWithNetworkError(loadingMessageId: Long) {
        val networkErrorResponse = errorResponseProvider.createNetworkError()
        chatDao.setErrorIntoMessageById(
            loadingMessageId,
            networkErrorResponse.header,
            networkErrorResponse.message
        )
    }

    override suspend fun replaceLoadingWithResponse(
        responseResult: ChatResponseResult.Success,
        loadingMessageId: Long
    ) {
        chatDao.setResponseIntoMessageById(
            loadingMessageId,
            responseResult.response.text,
            responseResult.response.reasoning
        )
    }

    override suspend fun sendQuery(
        model: AiModel,
        query: Query,
        context: List<Message>?,
        apiKey: String
    ): ChatResponseResult {
        val requestWithContext = buildList {
            context?.let { addAll(messageMapper.mapMessagesToDto(it)) }
            add(messageMapper.mapQueryToDto(query))
        }

        val requestBodyDto = RequestBodyDto(model.queryName, requestWithContext)
        val apiResponse = apiService.createAiResponse(requestBodyDto, apiKey)
        val chatResponseResult = apiResponseMapper.mapApiResponseToResult(apiResponse)

        return chatResponseResult
    }
}
