package com.example.ghorongo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.ghorongo.ui.screens.auth.CheckEmailScreen
import com.example.ghorongo.ui.screens.auth.ForgotPasswordScreen
import com.example.ghorongo.ui.screens.auth.LoginScreen
import com.example.ghorongo.ui.screens.auth.SignUpScreen
import com.example.ghorongo.ui.screens.homescreen.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "auth") {
        navigation(startDestination = "login", route = "auth") {
            composable("login") {
                LoginScreen(navController = navController)
            }
            composable("signup") {
                SignUpScreen(navController = navController)
            }
            composable("forgot_password") {
                ForgotPasswordScreen(navController = navController)
            }
            composable("check_email") {
                CheckEmailScreen(navController = navController)
            }
        }
        composable("home") {
            HomeScreen()
        }
    }
}