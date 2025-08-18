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
import com.example.ghorongo.data.model.Result
import com.example.ghorongo.ui.screens.profile.EditTenantProfileScreen
import com.example.ghorongo.ui.screens.profile.TenantProfileScreen
import com.example.ghorongo.viewmodel.TenantProfileViewModel
import com.example.ghorongo.viewmodel.TenantProfileViewModelFactory

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = Firebase.auth
    val userRepository = UserRepository()

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(userRepository)
    )

    var initialRoute by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        initialRoute = if (auth.currentUser != null) {
            val userId = auth.currentUser?.uid ?: ""
            val type = try {
                when (val result = userRepository.getUserType(userId)) {
                    is Result.Success -> result.data
                    is Result.Failure -> "tenant" // Default to tenant if error occurs
                }
            } catch (e: Exception) {
                "tenant"
            }
            authViewModel.updateUserType(type)
            "main_graph" // Go directly to main graph if logged in
        } else {
            "auth" // Go to auth flow if not logged in
        }
    }

    if (initialRoute == null) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
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
        }

        // Main graph
        navigation(startDestination = "dashboard", route = "main_graph") {
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

            composable(
                "room_detail/{roomId}",
                arguments = listOf(navArgument("roomId") {
                    type = androidx.navigation.NavType.IntType
                })
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getInt("roomId") ?: -1
                RoomDetailScreen(roomId = roomId)
            }

            composable("tenantProfile") {
                TenantProfileScreen(navController = navController)
            }
            composable("editTenantProfile/{tenantId}") { backStackEntry ->
                val tenantId = backStackEntry.arguments?.getString("tenantId") ?: ""

                val viewModel: TenantProfileViewModel = viewModel(
                    factory = TenantProfileViewModelFactory(UserRepository())
                )

                LaunchedEffect(tenantId) {
                    viewModel.loadTenantProfile()
                }

                viewModel.tenant?.let { tenant ->
                    EditTenantProfileScreen(
                        navController = navController,
                        viewModel = viewModel,
                        tenant = tenant
                    )
                } ?: run {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}