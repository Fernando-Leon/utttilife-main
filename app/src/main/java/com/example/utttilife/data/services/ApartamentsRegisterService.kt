package com.example.utttilife.data.services

import com.example.utttilife.data.Responses.ResponsesRents.ApartamentRegisterRequest
import com.example.utttilife.data.Responses.ResponsesRents.ApartamentRegisterResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApartamentsRegisterService {
    @Headers("Content-Type: application/json")
    @POST("property")
    suspend fun RegisterApartaments(
        @Body request: ApartamentRegisterRequest
    ): ApartamentRegisterResponse
}