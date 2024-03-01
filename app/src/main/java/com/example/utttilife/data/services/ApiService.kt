package com.example.utttilife.data.services


import com.example.utttilife.data.Responses.ResponsesLogin.BodyLoginResponse
import com.example.utttilife.data.Responses.ResponsesLogin.UserResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("login")
     suspend fun validateUser(
      @Body request: BodyLoginResponse
    ): UserResponse
}
