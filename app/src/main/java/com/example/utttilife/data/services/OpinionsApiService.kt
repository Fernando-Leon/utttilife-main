package com.example.utttilife.data.services

import com.example.utttilife.data.Responses.ResponsesRents.Opinion
import com.example.utttilife.data.Responses.ResponsesRents.OpinionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpinionsApiService {
    @Headers("Content-Type: application/json")
    @POST("/api/v1.0/property/opinion")
    suspend fun setBuildingOpinion(
        @Body request: Opinion
    ): Response<OpinionResponse>
}