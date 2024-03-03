package com.example.utttilife.data.services

import com.example.utttilife.data.Responses.ResponsesRents.BuildingsResponse
import retrofit2.Response
import retrofit2.http.GET

interface BuildingsApiService {
    @GET("api/v1.0/property")
    suspend fun getBuildings(): Response<List<BuildingsResponse>>

}