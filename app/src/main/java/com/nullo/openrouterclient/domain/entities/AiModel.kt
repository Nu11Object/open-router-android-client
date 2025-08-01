package com.nullo.openrouterclient.domain.entities

data class AiModel(
    val id: Long,
    val name: String,
    val queryName: String,
    val supportsReasoning: Boolean,
    val freeToUse: Boolean,
)
