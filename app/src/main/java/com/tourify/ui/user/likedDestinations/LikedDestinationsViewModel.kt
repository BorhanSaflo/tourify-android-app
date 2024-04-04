package com.tourify.ui.user.likedDestinations

import androidx.lifecycle.MutableLiveData
import com.tourify.models.DestinationResult
import com.tourify.repository.MainRepository
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.BaseViewModel
import com.tourify.viewmodels.CoroutinesErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LikedDestinationsViewModel @Inject constructor(
    private val mainRepository: MainRepository,
): BaseViewModel() {

    private val _likedDestinationsResponse = MutableLiveData<ApiResponse<List<DestinationResult>>>()
    val likedDestinationsResponse = _likedDestinationsResponse

    fun getLikedDestinations(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _likedDestinationsResponse,
        coroutinesErrorHandler,
    ) {
        mainRepository.getLikedDestinations()
    }
}