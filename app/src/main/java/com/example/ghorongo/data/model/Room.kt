package com.example.ghorongo.data.model

data class Room(
    val id: Int,
    val name: String,
    val title: String = "",
    val price: Int = 0,
    val imageUrl: String = "",
    val roomType: String = "",
    // Add other fields
)