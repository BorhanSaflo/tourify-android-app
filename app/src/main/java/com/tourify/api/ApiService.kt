package com.tourify.api

import com.tourify.ui.home.Destination
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("destinations/trending")
    fun getTrendingDestinations(): Call<List<Destination>>

    @GET("destinations/most-liked")
    fun getMostLikedDestinations(): Call<List<Destination>>
}