package com.example.ghorongo.presentation.auth

data class Room(val id: Int, val name: String)

val sampleRooms = listOf(
    Room(1, "Living Room"),
    Room(2, "Dining Room"),
    Room(3, "Bedroom"),
    Room(4, "Kitchen"),
    Room(5, "Guest Room")
)
