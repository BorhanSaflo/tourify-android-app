package com.tourify.api

import com.tourify.models.Destination
import com.tourify.models.DestinationResult
import com.tourify.models.UserInfoResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("destinations/trending")
    suspend fun getTrendingDestinations(): Response<List<DestinationResult>>

    @GET("destinations/most-viewed")
    suspend fun getMostViewedDestinations(): Response<List<DestinationResult>>

    @GET("destinations/most-liked")
    suspend fun getMostLikedDestinations(): Response<List<DestinationResult>>

    @GET("destination/{id}")
    suspend fun getDestination(@Path("id") id: Int): Response<Destination>

    @GET("user/info")
    suspend fun getUserInfo(): Response<UserInfoResponse>
}