package com.example.ghorongo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.ghorongo.ui.screens.auth.CheckEmailScreen
import com.example.ghorongo.ui.screens.auth.ForgotPasswordScreen
import com.example.ghorongo.ui.screens.auth.LoginScreen
import com.example.ghorongo.ui.screens.auth.SignUpScreen
import com.example.ghorongo.ui.screens.homescreen.HomeScreen
import com.example.ghorongo.ui.theme.GhorOnGoTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🧪 Sign out for testing so you see Login screen
       // Firebase.auth.signOut()  // ⛔ REMOVE THIS in production

        setContent {
            GhorOnGoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = Firebase.auth
    var initialRoute by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        initialRoute = if (auth.currentUser != null) {
            "home"
        } else {
            "auth"
        }
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
        navigation(startDestination = "login", route = "auth") {
            composable("login") { LoginScreen(navController) }
            composable("signup") { SignUpScreen(navController) }
            composable("forgot_password") { ForgotPasswordScreen(navController) }
            composable("check_email") { CheckEmailScreen(navController) }
        }

        composable("home") { HomeScreen() }
    }
}
