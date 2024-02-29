package com.example.utttilife.data.services

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Body

interface GptService {
    @POST("path/to/api")
    suspend fun getResponse(@Body request: GptRequest): GptResponse
}

data class GptRequest(val prompt: String)
data class GptResponse(val responses: List<String>)