package com.tourify.ui.user.dislikedDestinations

import androidx.lifecycle.MutableLiveData
import com.tourify.models.DestinationResult
import com.tourify.repository.MainRepository
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.BaseViewModel
import com.tourify.viewmodels.CoroutinesErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DislikedDestinationsViewModel @Inject constructor(
    private val mainRepository: MainRepository,
): BaseViewModel() {

    private val _dislikedDestinationsResponse = MutableLiveData<ApiResponse<List<DestinationResult>>>()
    val dislikedDestinationsResponse = _dislikedDestinationsResponse

    fun getDislikedDestinations(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _dislikedDestinationsResponse,
        coroutinesErrorHandler,
    ) {
        mainRepository.getDislikedDestinations()
    }
}