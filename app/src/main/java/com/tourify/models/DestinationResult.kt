package com.tourify.models

data class DestinationResult(
    val id: String,
    val name: String,
    val country: String,
    val views: Int,
    val likes: Int,
    val thumbnail: String,
    val description: String,
)