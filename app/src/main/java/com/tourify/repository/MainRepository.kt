package com.tourify.repository

import com.tourify.api.ApiService
import com.tourify.utils.apiRequestFlow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val mainApiService: ApiService,
) {
    fun getUserInfo() = apiRequestFlow {
        mainApiService.getUserInfo()
    }

    fun getFeaturedDestination() = apiRequestFlow {
        mainApiService.getFeaturedDestinations()
    }

    fun getTrendingDestinations() = apiRequestFlow {
        mainApiService.getTrendingDestinations()
    }

    fun getMostViewedDestinations() = apiRequestFlow {
        mainApiService.getMostViewedDestinations()
    }

    fun getMostLikedDestinations() = apiRequestFlow {
        mainApiService.getMostLikedDestinations()
    }

    fun getDestination(id: Int) = apiRequestFlow {
        mainApiService.getDestination(id)
    }

    fun searchDestinations(query: String) = apiRequestFlow {
        mainApiService.searchDestinations(query)
    }

    fun exploreDestinations(query: String) = apiRequestFlow {
        mainApiService.exploreDestinations(query)
    }

    fun getSavedDestinations() = apiRequestFlow {
        mainApiService.getSavedDestinations()
    }

    fun saveDestination(id: Int) = apiRequestFlow {
        mainApiService.saveDestination(id)
    }

    fun likeDestination(id: Int) = apiRequestFlow {
        mainApiService.likeDestination(id)
    }

    fun dislikeDestination(id: Int) = apiRequestFlow {
        mainApiService.dislikeDestination(id)
    }
}