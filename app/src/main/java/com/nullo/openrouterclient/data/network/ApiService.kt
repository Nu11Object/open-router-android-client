package com.nullo.openrouterclient.data.network

import com.nullo.openrouterclient.data.network.dto.chat.ChatResponseDto
import com.nullo.openrouterclient.data.network.dto.chat.RequestBodyDto
import com.nullo.openrouterclient.data.network.dto.models.AiModelsResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("chat/completions")
    suspend fun createAiResponse(
        @Body requestBodyDto: RequestBodyDto,
        @Header("Authorization") apiKey: String,
    ): Response<ChatResponseDto>

    @GET("models")
    suspend fun getModels(): Response<AiModelsResponseDto>
}
