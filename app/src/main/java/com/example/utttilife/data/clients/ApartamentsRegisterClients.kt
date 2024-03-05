package com.example.utttilife.data.clients

import com.example.utttilife.data.services.ApartamentsRegisterService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApartamentsRegisterClients {
    private const val BASE_URL = "https://api-utttilife.azurewebsites.net/api/v1.0/"
    fun ApartamentsRegisterCli(): ApartamentsRegisterService {
        val retrofit2 = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit2.create(ApartamentsRegisterService::class.java)
    }
}