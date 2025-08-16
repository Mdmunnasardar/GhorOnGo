package com.example.ghorongo.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ghorongo.data.repository.UserRepository
import com.example.ghorongo.ui.component.auth.UserTypeSelectionScreen
import com.example.ghorongo.ui.screens.auth.CheckEmailScreen
import com.example.ghorongo.ui.screens.auth.ForgotPasswordScreen
import com.example.ghorongo.ui.screens.auth.LoginScreen
import com.example.ghorongo.ui.screens.auth.SignUpScreen
import com.example.ghorongo.ui.screens.dashboard.RoomDetailScreen
import com.example.ghorongo.ui.screens.homescreen.MainScreen
import com.example.ghorongo.viewmodel.AuthViewModel
import com.example.ghorongo.viewmodel.AuthViewModelFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = Firebase.auth

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(UserRepository())
    )

    var initialRoute by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        initialRoute = if (auth.currentUser != null) "main_graph" else "auth"
    }

    if (initialRoute == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    NavHost(
        navController = navController,
        startDestination = initialRoute!!
    ) {
        // Auth graph
        navigation(startDestination = "login", route = "auth") {
            composable("login") { LoginScreen(navController, authViewModel) }
            composable("signup") { SignUpScreen(navController, authViewModel) }
            composable("forgot_password") { ForgotPasswordScreen(navController, authViewModel) }
            composable("check_email") { CheckEmailScreen(navController) }
            composable("user_type_selection") { UserTypeSelectionScreen(navController, authViewModel) }
        }

        // Main graph
        navigation(startDestination = "dashboard", route = "main_graph") {
            // Bottom nav screens
            composable("dashboard") {
                MainScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    startRoute = "dashboard"
                )
            }
            composable("profile") {
                MainScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    startRoute = "profile"
                )
            }
            composable("saved") {
                MainScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    startRoute = "saved"
                )
            }
            composable("message") {
                MainScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    startRoute = "message"
                )
            }

            // Detail screens
            composable(
                "room_detail/{roomId}",
                arguments = listOf(navArgument("roomId") { type = androidx.navigation.NavType.IntType })
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getInt("roomId") ?: -1
                RoomDetailScreen(roomId = roomId)
            }
        }
    }
}
