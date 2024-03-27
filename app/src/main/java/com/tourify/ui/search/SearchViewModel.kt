package com.tourify.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tourify.models.DestinationResult
import com.tourify.repository.MainRepository
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.BaseViewModel
import com.tourify.viewmodels.CoroutinesErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mainRepository: MainRepository,
): BaseViewModel() {

    private val _searchDestinationsResponse = MutableLiveData<ApiResponse<List<DestinationResult>>>()
    val searchResultsResponse: LiveData<ApiResponse<List<DestinationResult>>> = _searchDestinationsResponse

    fun searchDestinations(query: String, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _searchDestinationsResponse,
        coroutinesErrorHandler,
    ) {
        mainRepository.searchDestinations(query)
    }
}