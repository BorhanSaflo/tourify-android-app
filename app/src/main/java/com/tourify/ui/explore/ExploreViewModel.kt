package com.tourify.ui.explore

import androidx.lifecycle.MutableLiveData
import com.tourify.models.DestinationResult
import com.tourify.repository.MainRepository
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.BaseViewModel
import com.tourify.viewmodels.CoroutinesErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val mainRepository: MainRepository,
): BaseViewModel() {
    private val _exploreDestinationsResponse = MutableLiveData<ApiResponse<List<DestinationResult>>>()
    val exploreDestinationsResponse = _exploreDestinationsResponse

    fun exploreDestinations(query: String, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _exploreDestinationsResponse,
        coroutinesErrorHandler,
    ) {
        mainRepository.exploreDestinations(query)
    }
}