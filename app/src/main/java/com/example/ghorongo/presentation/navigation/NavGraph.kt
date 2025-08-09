package com.example.ghorongo.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ghorongo.ui.screens.booking.SavedScreen
import com.example.ghorongo.ui.screens.chat.MessageScreen
import com.example.ghorongo.ui.screens.homescreen.HomeScreen
import com.example.ghorongo.ui.screens.profile.ProfileScreen



@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen( ) }
        composable("profile") { ProfileScreen() }
        composable("saved") { SavedScreen() }
        composable("message") { MessageScreen() }
    }
}


