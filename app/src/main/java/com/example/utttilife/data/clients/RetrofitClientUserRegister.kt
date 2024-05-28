package com.example.utttilife.data.clients


import com.example.utttilife.data.services.ApiServiceRegisterUsers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientUserRegister {
        private const val BASE_URL = "https://api-utttilife.onrender.com/api/v1.0/auth/"

    fun createUser(): ApiServiceRegisterUsers {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiServiceRegisterUsers::class.java)
    }
}