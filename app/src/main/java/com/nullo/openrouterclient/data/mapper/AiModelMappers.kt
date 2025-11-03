package com.nullo.openrouterclient.data.mapper

import com.nullo.openrouterclient.data.database.aiModels.AiModelDbEntity
import com.nullo.openrouterclient.domain.entities.AiModel

fun AiModel.toDbEntity(): AiModelDbEntity = AiModelDbEntity(
    id = id,
    name = name,
    queryName = queryName,
    supportsReasoning = supportsReasoning,
    freeToUse = freeToUse
)

fun AiModelDbEntity.toAiModel(): AiModel = AiModel(
    id = id,
    name = name,
    queryName = queryName,
    supportsReasoning = supportsReasoning,
    freeToUse = freeToUse
)
