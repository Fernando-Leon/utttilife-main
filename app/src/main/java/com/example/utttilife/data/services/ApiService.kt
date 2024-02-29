package com.example.utttilife.data.services


import com.example.utttilife.Data.Responses.LoginResponse
import com.example.utttilife.Data.Responses.UserResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("login")
     suspend fun validateUser(
      @Body request: LoginResponse
    ): UserResponse
}
