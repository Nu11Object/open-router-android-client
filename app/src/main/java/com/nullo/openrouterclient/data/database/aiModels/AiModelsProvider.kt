package com.nullo.openrouterclient.data.database.aiModels

import com.nullo.openrouterclient.data.Constants.UNDEFINED_ID
import javax.inject.Inject

class AiModelsProvider @Inject constructor() {

    val defaultModels = listOf(
        AiModelDbEntity(
            UNDEFINED_ID,
            "DeepSeek V3",
            "deepseek/deepseek-chat-v3-0324:free",
            supportsReasoning = false,
            freeToUse = true,
        ),
        AiModelDbEntity(
            UNDEFINED_ID,
            "DeepSeek R1",
            "deepseek/deepseek-r1-0528:free",
            supportsReasoning = true,
            freeToUse = true,
        ),
        AiModelDbEntity(
            UNDEFINED_ID,
            "Gemini 2.0 Flash",
            "google/gemini-2.0-flash-exp:free",
            supportsReasoning = false,
            freeToUse = true,
        ),
        AiModelDbEntity(
            UNDEFINED_ID,
            "Qwen3 235B",
            "qwen/qwen3-235b-a22b-2507:free",
            supportsReasoning = false,
            freeToUse = true,
        ),
        AiModelDbEntity(
            UNDEFINED_ID,
            "Qwen: QwQ 32B",
            "qwen/qwq-32b:free",
            supportsReasoning = true,
            freeToUse = true,
        ),
    )

    fun getDefaultModel() = defaultModels.first()
}
