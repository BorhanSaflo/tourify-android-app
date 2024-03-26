package com.tourify.ui.destination

import androidx.lifecycle.MutableLiveData
import com.tourify.models.Destination
import com.tourify.repository.MainRepository
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.BaseViewModel
import com.tourify.viewmodels.CoroutinesErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DestinationViewModel @Inject constructor(
    private val mainRepository: MainRepository,
): BaseViewModel() {

    private val _destinationResponse = MutableLiveData<ApiResponse<Destination>>()
    val destinationResponse = _destinationResponse

    fun getDestination(id: Int, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _destinationResponse,
        coroutinesErrorHandler,
    ) {
        mainRepository.getDestination(id)
    }
}