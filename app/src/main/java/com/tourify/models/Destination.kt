package com.tourify.models

data class Destination(
    val id: Int,
    val name: String,
    val country: String,
    val description: String?,
    val googlePlaceId: String,
    val images: List<String>,
    val likes: Int,
    val dislikes: Int,
    val views: Int,
    val reviewsQuery: List<Review>
)

data class Review(
    val id: Int,
    val comment: String,
    val timestamp: String?,
    val user: User
)

data class User(
    val id: Int,
    val name: String
)
