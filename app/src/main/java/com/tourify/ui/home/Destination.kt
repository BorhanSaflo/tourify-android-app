package com.tourify.ui.home

data class Destination(
    val id: String,
    val name: String,
    val country: String,
    val thumbnail: String,
    val description: String,
    val images: List<String>
)