package com.tourify.api

import com.tourify.models.Auth
import com.tourify.models.LoginResponse
import com.tourify.models.Registration
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(
        @Body auth: Auth,
    ): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(
        @Body auth: Registration,
    ): Response<LoginResponse>

    @GET("auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") token: String,
    ): Response<LoginResponse>
}