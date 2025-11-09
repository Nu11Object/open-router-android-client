package com.nullo.openrouterclient.data.network.dto.models

import com.google.gson.annotations.SerializedName

data class AiModelsResponseDto(
    @SerializedName("data")
    val aiModels: List<AiModelDto>
)

data class AiModelDto(
    @SerializedName("id")
    val queryName: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("pricing")
    val pricing: PricingDto,
    @SerializedName("supported_parameters")
    val supportedParameters: List<String>?,
    @SerializedName("architecture")
    val architectureDto: ArchitectureDto,
)

data class PricingDto(
    @SerializedName("prompt")
    val prompt: String,
    @SerializedName("completion")
    val completion: String,
    @SerializedName("request")
    val request: String,
)

data class ArchitectureDto(
    @SerializedName("modality")
    val modality: String,
)
