package com.tourify.models

data class ErrorResponse(
    val errorMessage: String,
    val code: Int
)