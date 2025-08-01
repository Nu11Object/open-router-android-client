package com.nullo.openrouterclient.data.database.aiModels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_models")
data class AiModelDbEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val queryName: String,
    val supportsReasoning: Boolean,
    val freeToUse: Boolean,
)
