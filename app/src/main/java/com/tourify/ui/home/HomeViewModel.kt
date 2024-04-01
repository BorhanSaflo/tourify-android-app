package com.tourify.ui.home

import androidx.lifecycle.MutableLiveData
import com.tourify.models.DestinationResult
import com.tourify.models.UserInfoResponse
import com.tourify.repository.MainRepository
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.BaseViewModel
import com.tourify.viewmodels.CoroutinesErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: MainRepository,
): BaseViewModel() {

    private val _userInfoResponse = MutableLiveData<ApiResponse<UserInfoResponse>>()
    val userInfoResponse = _userInfoResponse

    private val _featuredDestinationResponse = MutableLiveData<ApiResponse<DestinationResult>>()
    val featuredDestinationResponse = _featuredDestinationResponse

    private val _trendingDestinationsResponse = MutableLiveData<ApiResponse<List<DestinationResult>>>()
    val trendingDestinationsResponse = _trendingDestinationsResponse

    private val _mostLikedDestinationsResponse = MutableLiveData<ApiResponse<List<DestinationResult>>>()
    val mostLikedDestinationsResponse = _mostLikedDestinationsResponse

    private val _mostViewedDestinationsResponse = MutableLiveData<ApiResponse<List<DestinationResult>>>()
    val mostViewedDestinationsResponse = _mostViewedDestinationsResponse

    fun getUserInfo(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _userInfoResponse,
        coroutinesErrorHandler,
    ) {
        mainRepository.getUserInfo()
    }

    fun getFeaturedDestinations(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _featuredDestinationResponse,
        coroutinesErrorHandler,
    ) {
        mainRepository.getFeaturedDestination()
    }

    fun getTrendingDestinations(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _trendingDestinationsResponse,
        coroutinesErrorHandler,
    ) {
        mainRepository.getTrendingDestinations()
    }

    fun getMostLikedDestinations(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _mostLikedDestinationsResponse,
        coroutinesErrorHandler,
    ) {
        mainRepository.getMostLikedDestinations()
    }

    fun getMostViewedDestinations(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _mostViewedDestinationsResponse,
        coroutinesErrorHandler,
    ) {
        mainRepository.getMostViewedDestinations()
    }
}