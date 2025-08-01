package com.nullo.openrouterclient.data.network.dto.chat

import com.google.gson.annotations.SerializedName

data class ErrorResponseDto(
    @SerializedName("error")
    val error: ErrorDto
)

data class ErrorDto(
    @SerializedName("message")
    val message: String,
    @SerializedName("metadata")
    val metadata: MetadataDto?,
)

data class MetadataDto(
    @SerializedName("raw")
    val raw: String
)
