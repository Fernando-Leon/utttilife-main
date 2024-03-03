package com.example.utttilife.data.clients

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApartmentsClient {
    fun makeApartmentsService(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api-utttilife.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}