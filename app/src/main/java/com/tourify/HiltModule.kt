package com.tourify

import com.tourify.api.ApiService
import com.tourify.repository.AuthRepository
import com.tourify.repository.MainRepository
import com.tourify.api.AuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {

    @Provides
    fun provideAuthRepository(authApiService: AuthApiService) = AuthRepository(authApiService)

    @Provides
    fun provideMainRepository(mainApiService: ApiService) = MainRepository(mainApiService)
}