package com.example.ghorongo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ghorongo.presentation.auth.HomeScreen
import com.example.ghorongo.presentation.auth.LoginScreen
import com.example.ghorongo.presentation.auth.SignUpScreen


@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = "login"  // Add this parameter with default value
) {
    NavHost(
        navController = navController,
        startDestination = startDestination  // Use the parameter here
    ) {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("signup") {
            SignUpScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
    }
}