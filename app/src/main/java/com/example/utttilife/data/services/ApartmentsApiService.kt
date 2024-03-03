package com.example.utttilife.data.services

import com.example.utttilife.data.Responses.ResponsesRents.ApartmentsRequest
import com.example.utttilife.data.Responses.ResponsesRents.ApartmentsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApartmentsApiService {
    @Headers("Content-Type: application/json")
    @POST("/api/v1.0/property/apartments-coordinates")
    suspend fun getApartmentsCoordinates(
        @Body request: ApartmentsRequest
    ): Response<List<ApartmentsResponse>>
}