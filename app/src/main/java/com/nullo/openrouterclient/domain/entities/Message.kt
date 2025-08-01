package com.nullo.openrouterclient.domain.entities

sealed interface Message {

    val id: Long
    val text: String

    data class Query(
        override val id: Long,
        override val text: String,
    ) : Message

    data class Loading(
        override val id: Long,
        override val text: String,
    ) : Message

    data class Error(
        override val id: Long,
        override val text: String,
        val header: String,
    ) : Message

    data class AiResponse(
        override val id: Long,
        override val text: String,
        val reasoning: String? = null,
    ) : Message
}
