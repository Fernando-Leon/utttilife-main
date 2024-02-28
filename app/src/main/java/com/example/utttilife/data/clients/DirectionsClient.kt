package com.example.utttilife.data.clients

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DirectionsClient {
    fun makeDirectionsService(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}