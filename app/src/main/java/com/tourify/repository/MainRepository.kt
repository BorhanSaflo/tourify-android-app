package com.tourify.repository

import com.tourify.api.ApiService
import com.tourify.utils.apiRequestFlow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val mainApiService: ApiService,
) {
    fun getUserInfo() = apiRequestFlow {
        mainApiService.getUserInfo()
    }
}