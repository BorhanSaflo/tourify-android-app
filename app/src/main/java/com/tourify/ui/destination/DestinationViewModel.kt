package com.tourify.ui.destination

import androidx.lifecycle.MutableLiveData
import com.tourify.models.Destination
import com.tourify.models.DestinationResult
import com.tourify.repository.MainRepository
import com.tourify.utils.ApiResponse
import com.tourify.viewmodels.BaseViewModel
import com.tourify.viewmodels.CoroutinesErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
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

    fun getRelativeTime(timeStamp: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val parsedDate = sdf.parse(timeStamp) ?: return "Invalid date"

        val now = Calendar.getInstance().time
        val diff = now.time - parsedDate.time

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7
        val months = days / 30 // Approximation
        val years = days / 365 // Approximation

        return when {
            years > 0 -> "${years}yr ago"
            months > 0 -> "${months}mo ago"
            weeks > 0 -> "${weeks}w ago"
            days > 0 -> "${days}d ago"
            hours > 0 -> "${hours}h ago"
            minutes > 0 -> "${minutes}m ago"
            else -> "${seconds}s ago"
        }
    }

    private val _savedDestinationsResponse = MutableLiveData<ApiResponse<List<DestinationResult>>>()
    val savedDestinationsResponse = _savedDestinationsResponse

    fun getSavedDestinations(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _savedDestinationsResponse,
        coroutinesErrorHandler,
    ) {
        mainRepository.getSavedDestinations()
    }

    private val _saveDestinationResponse = MutableLiveData<ApiResponse<Unit>>()
    val saveDestinationResponse = _saveDestinationResponse

    fun saveDestination(id: Int, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _saveDestinationResponse,
        coroutinesErrorHandler,
    ) {
        mainRepository.saveDestination(id)
    }
}