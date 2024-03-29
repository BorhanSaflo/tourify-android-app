package com.tourify.api

import com.tourify.models.Destination
import com.tourify.models.DestinationResult
import com.tourify.models.UserInfoResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("search/{query}")
    suspend fun searchDestinations(@Path("query") query: String): Response<List<DestinationResult>>

    @GET("explore/")
    suspend fun exploreDestinations(@Query("tags") query: String): Response<List<DestinationResult>>

    @GET("user/saved")
    suspend fun getSavedDestinations(): Response<List<DestinationResult>>

    @POST("save/{id}")
    suspend fun saveDestination(@Path("id") id: Int): Response<Unit>
}