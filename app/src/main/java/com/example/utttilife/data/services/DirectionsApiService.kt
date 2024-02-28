package com.example.utttilife.data.services

import com.example.utttilife.models.RouteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsApiService {
    @GET("/v2/directions/driving-car")
    suspend fun getDirections(
        @Query("api_key") key: String,
        @Query("start", encoded = true) start: String,
        @Query("end", encoded = true) end: String
    ): Response<RouteResponse>
}