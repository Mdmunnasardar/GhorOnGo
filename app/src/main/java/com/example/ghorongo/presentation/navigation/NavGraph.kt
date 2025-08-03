package com.example.ghorongo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ghorongo.ui.screens.auth.CheckEmailScreen
import com.example.ghorongo.ui.screens.auth.ForgotPasswordScreen

import com.example.ghorongo.ui.screens.auth.HomeScreen
import com.example.ghorongo.ui.screens.auth.LoginScreen
import com.example.ghorongo.ui.screens.auth.SignUpScreen

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
        composable("forgot_password") {
            ForgotPasswordScreen(navController = navController)
        }
        composable("check_email") {
            CheckEmailScreen(navController)
        }
    }
}