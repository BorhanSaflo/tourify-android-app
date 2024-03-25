package com.tourify.repository

import com.tourify.models.Auth
import com.tourify.service.auth.AuthApiService
import com.tourify.utils.apiRequestFlow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
) {
    fun login(auth: Auth) = apiRequestFlow {
        authApiService.login(auth)
    }
}