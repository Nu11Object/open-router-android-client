package com.nullo.openrouterclient.data.network.dto.chat

import com.google.gson.annotations.SerializedName

data class RequestBodyDto(
    @SerializedName("model")
    val modelQueryName: String,
    @SerializedName("messages")
    val messages: List<QueryDto>,
)

data class QueryDto(
    @SerializedName("role")
    val role: String,
    @SerializedName("content")
    val content: String,
)
