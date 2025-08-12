package com.example.ghorongo.ui.screens.room

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ghorongo.data.model.Room

@Composable
fun RoomDetailScreen(roomId: Int) {

    val room = remember(roomId) {
        listOf(
            Room(1, "Living Room"),
            Room(2, "Dining Room"),
            Room(3, "Bedroom"),
            Room(4, "Kitchen"),
            Room(5, "Guest Room")
        ).find { it.id == roomId }
    }

    if (room == null) {
        Text("Room not found")
        return
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Room Name: ${room.name}", style = MaterialTheme.typography.titleLarge)
        Text(text = "Room Type: ${room.roomType}")
        Text(text = "Price: ${room.price}")

    }
}
