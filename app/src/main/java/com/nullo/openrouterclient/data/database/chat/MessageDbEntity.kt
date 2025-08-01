package com.nullo.openrouterclient.data.database.chat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageDbEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val text: String,
    val isLoading: Boolean,
    val role: String,
    val reasoning: String? = null,
    val error: String? = null,
)
