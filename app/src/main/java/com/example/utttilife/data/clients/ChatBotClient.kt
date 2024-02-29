package com.example.utttilife.data.clients

import com.example.utttilife.data.services.GptService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ChatBotClient {
    private const val BASE_URL = "https://api.openai.com/v1/"

    val service: GptService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GptService::class.java)
    }
}
