package com.example.utttilife.data.services


import com.example.utttilife.data.Responses.ResponsesUsers.BodyUserRegisterResponse
import com.example.utttilife.data.Responses.ResponsesUsers.UsersRegisterResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiServiceRegisterUsers {
    @Headers("Content-Type: application/json")
    @POST("signin")
    suspend fun CreateUsers(
        @Body request: BodyUserRegisterResponse
    ): UsersRegisterResponse
}