package com.nullo.openrouterclient.data.mapper

import com.nullo.openrouterclient.data.database.aiModels.AiModelDbEntity
import com.nullo.openrouterclient.domain.entities.AiModel
import javax.inject.Inject

class AiModelMapper @Inject constructor() {

    fun mapAiModelToDbEntity(aiModel: AiModel): AiModelDbEntity {
        return AiModelDbEntity(
            id = aiModel.id,
            name = aiModel.name,
            queryName = aiModel.queryName,
            supportsReasoning = aiModel.supportsReasoning,
            freeToUse = aiModel.freeToUse
        )
    }

    fun mapDbEntityToAiModel(aiModelDbEntity: AiModelDbEntity): AiModel {
        return AiModel(
            id = aiModelDbEntity.id,
            name = aiModelDbEntity.name,
            queryName = aiModelDbEntity.queryName,
            supportsReasoning = aiModelDbEntity.supportsReasoning,
            freeToUse = aiModelDbEntity.freeToUse
        )
    }
}
