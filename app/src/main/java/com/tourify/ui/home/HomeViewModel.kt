package com.tourify.ui.home

import androidx.lifecycle.MutableLiveData
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

    fun getUserInfo(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _userInfoResponse,
        coroutinesErrorHandler,
    ) {
        mainRepository.getUserInfo()
    }
}