package com.example.utttilife.data.clients


import com.example.utttilife.data.services.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientLogin {
    private const val BASE_URL = "https://api-utttilife.onrender.com/api/v1.0/auth/"

    fun create(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}

