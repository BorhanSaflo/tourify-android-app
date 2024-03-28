package com.tourify.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tourify.utils.ApiResponse
import kotlinx.coroutines.launch

class ExploreResultsViewModel : ViewModel() {
    private val _exploreResultsResponse = MutableLiveData<ApiResponse<List<String>>>()
    val exploreResultsResponse: LiveData<ApiResponse<List<String>>> = _exploreResultsResponse

    fun exploreDestinations(query: String) {
        viewModelScope.launch {
            _exploreResultsResponse.value = ApiResponse.Loading
            try {
                // TODO: Replace this with actual API call
                val results = listOf<String>() // Fetch results from API
                _exploreResultsResponse.value = ApiResponse.Success(results)
            } catch (e: Exception) {
                _exploreResultsResponse.value = ApiResponse.Failure(e.message ?: "An error occurred", 0)
            }
        }
    }
}