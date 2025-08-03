package com.example.ghorongo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.ghorongo.presentation.navigation.NavGraph
import com.example.ghorongo.ui.theme.GhorOnGoTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        Firebase.auth.setLanguageCode("en") // Set language for auth emails

        enableEdgeToEdge()
        setContent {
            GhorOnGoTheme {
                // Edge-to-edge content
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val auth = Firebase.auth

                    // Check if user is already logged in and verified
                    val currentUser = auth.currentUser
                    val startDestination = if (currentUser != null && currentUser.isEmailVerified) {
                        "home"
                    } else {
                        "login"
                    }

                    NavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user needs to be redirected after email verification
        Firebase.auth.currentUser?.reload()
    }
}