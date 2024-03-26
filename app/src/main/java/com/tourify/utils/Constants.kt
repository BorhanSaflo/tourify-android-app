package com.tourify.utils

import android.util.Log
import com.google.gson.Gson
import com.tourify.models.ErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

fun<T> apiRequestFlow(call: suspend () -> Response<T>): Flow<ApiResponse<T>> = flow {
    emit(ApiResponse.Loading)

    withTimeoutOrNull(20000L) {
        val response = call()

        try {
            if (response.isSuccessful) {
                response.body()?.let { data ->
                    emit(ApiResponse.Success(data))
                }
            } else {
                response.errorBody()?.let { error ->
                    val errorString = error.string() // Read the error content into a string
                    error.close() // Close the error body
        
                    // Now you can use errorString multiple times
                    val parsedError = Gson().fromJson(errorString, ErrorResponse::class.java)
                    Log.w("apiRequestFlow", "Error: $errorString")
                    Log.w("apiRequestFlow", "Error: ${parsedError.errorMessage}")
                    emit(ApiResponse.Failure(parsedError.errorMessage, parsedError.code))
                }
            }
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message ?: e.toString(), 400))
        }
    } ?: emit(ApiResponse.Failure("Timeout! Please try again.", 408))
}.flowOn(Dispatchers.IO)