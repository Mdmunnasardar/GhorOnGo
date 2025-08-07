package com.example.ghorongo.data.repository

import com.example.ghorongo.data.model.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getFeaturedRooms(): Flow<List<Room>> {
        return db.collection("rooms")
            .limit(10)
            .snapshots()
            .map { snapshot -> snapshot.toObjects(Room::class.java) }
    }
}
