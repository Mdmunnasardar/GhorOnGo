package com.example.ghorongo.ui.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.ghorongo.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val rooms = listOf(
        Room(
            "Modern Flat",
            "৳ 10,000 / month",
            "Khulna",
            "https://images.unsplash.com/photo-1507089947368-19c1da9775ae?auto=format&fit=crop&w=800&q=80"
        ),
        Room(
            "Family Apartment",
            "৳ 12,500 / month",
            "Khulna",
            "https://images.unsplash.com/photo-1568605114967-8130f3a36994?auto=format&fit=crop&w=800&q=80"
        ),
        Room(
            "Bachelor Room",
            "৳ 6,000 / month",
            "Khulna",
            "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?auto=format&fit=crop&w=800&q=80"
        ),
        Room(
            "Luxury Apartment",
            "৳ 15,000 / month",
            "Khulna",
            "https://images.unsplash.com/photo-1600585154154-98d8a73b8f3d?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    Button(
                        onClick = { authViewModel.logout(navController) }
                    ) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // Location Info
            Text(
                text = "📍 Location: Khulna",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF1976D2),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Section Title
            Text(
                "Suggested Rooms",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Room List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(rooms.size) { index ->
                    RoomCard(room = rooms[index])
                }
            }
        }
    }
}

// Room Data Class
data class Room(
    val title: String,
    val price: String,
    val location: String,
    val imageUrl: String
)

// Room Card Composable
@Composable
fun RoomCard(room: Room) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(model = room.imageUrl),
                contentDescription = room.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(room.title, style = MaterialTheme.typography.titleMedium)
                Text(room.price, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF1976D2))
                Text(room.location, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}
