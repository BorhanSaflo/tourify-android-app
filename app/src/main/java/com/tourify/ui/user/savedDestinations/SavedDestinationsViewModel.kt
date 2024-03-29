package com.tourify.ui.user.savedDestinations

import androidx.lifecycle.MutableLiveData
import com.tourify.models.DestinationResult
import com.tourify.repository.MainRepository
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.BaseViewModel
import com.tourify.viewmodels.CoroutinesErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedDestinationsViewModel @Inject constructor(
    private val mainRepository: MainRepository,
): BaseViewModel() {

    private val _savedDestinationsResponse = MutableLiveData<ApiResponse<List<DestinationResult>>>()
    val savedDestinationsResponse = _savedDestinationsResponse

    fun getSavedDestinations(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _savedDestinationsResponse,
        coroutinesErrorHandler,
    ) {
        mainRepository.getSavedDestinations()
    }
}