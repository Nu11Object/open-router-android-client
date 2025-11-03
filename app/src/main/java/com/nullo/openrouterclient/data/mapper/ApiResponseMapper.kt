package com.nullo.openrouterclient.data.mapper

import com.google.gson.Gson
import com.nullo.openrouterclient.data.Constants.UNDEFINED_ID
import com.nullo.openrouterclient.data.ErrorResponseProvider
import com.nullo.openrouterclient.data.mapper.ApiResponseMapper.Companion.KEY_SUPPORTS_REASONING
import com.nullo.openrouterclient.data.mapper.ApiResponseMapper.Companion.PRICE_IF_FREE
import com.nullo.openrouterclient.data.network.dto.chat.ChatResponseDto
import com.nullo.openrouterclient.data.network.dto.chat.ErrorDto
import com.nullo.openrouterclient.data.network.dto.chat.ErrorResponseDto
import com.nullo.openrouterclient.data.network.dto.chat.MetadataDto
import com.nullo.openrouterclient.data.network.dto.models.AiModelsResponseDto
import com.nullo.openrouterclient.domain.entities.AiModel
import com.nullo.openrouterclient.domain.entities.ChatResponseResult
import com.nullo.openrouterclient.domain.entities.Message.AiResponse
import retrofit2.Response
import javax.inject.Inject

fun AiModelsResponseDto.toAiModels(): List<AiModel> = aiModels.map {
    val supportsReasoning = it.supportedParameters?.contains(KEY_SUPPORTS_REASONING)
    val freeToUse = with(it.pricing) {
        prompt == PRICE_IF_FREE && request == PRICE_IF_FREE && completion == PRICE_IF_FREE
    }
    AiModel(
        id = UNDEFINED_ID,
        name = it.name,
        queryName = it.queryName,
        supportsReasoning = supportsReasoning ?: false,
        freeToUse = freeToUse
    )
}

fun ErrorResponseDto.toDisplayString(): String = with(error) {
    buildString {
        append(message)
        metadata?.raw?.let { append("\nRaw message: $it") }
    }
}

class ApiResponseMapper @Inject constructor(
    private val errorResponseProvider: ErrorResponseProvider,
    private val gson: Gson,
) {

    val unknownError by lazy {
        errorResponseProvider.createUnknownError()
    }

    fun mapApiResponseToResult(response: Response<ChatResponseDto>): ChatResponseResult {
        if (response.isSuccessful) {
            val bodyMessage = response.body()?.choices?.firstOrNull()?.message
                ?: return unknownError
            return ChatResponseResult.Success(
                AiResponse(
                    text = bodyMessage.content,
                    reasoning = bodyMessage.reasoning,
                    id = UNDEFINED_ID
                )
            )
        } else {
            val errorBody = response.errorBody()?.string()
            val errorResponseDto = runCatching {
                gson.fromJson(errorBody, ErrorResponseDto::class.java)
            }.getOrDefault(
                ErrorResponseDto(
                    ErrorDto(
                        message = unknownError.header,
                        metadata = MetadataDto(errorBody ?: "")
                    )
                )
            )
            return ChatResponseResult.Error(
                unknownError.header,
                errorResponseDto.toDisplayString()
            )
        }
    }

    companion object {

        const val KEY_SUPPORTS_REASONING = "reasoning"
        const val PRICE_IF_FREE = "0"
    }
}
