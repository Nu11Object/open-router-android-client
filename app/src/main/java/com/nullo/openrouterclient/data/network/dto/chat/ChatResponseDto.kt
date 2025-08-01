package com.nullo.openrouterclient.data.network.dto.chat

import com.google.gson.annotations.SerializedName

data class ChatResponseDto(
    @SerializedName("choices")
    val choices: List<ChoiceDto>? = null,
)

data class ChoiceDto(
    @SerializedName("message")
    val message: MessageDto
)

data class MessageDto(
    @SerializedName("content")
    val content: String,
    @SerializedName("reasoning")
    val reasoning: String?
)
