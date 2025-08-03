package com.example.ghorongo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.ghorongo.presentation.navigation.NavGraph
import com.example.ghorongo.ui.theme.GhorOnGoTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth.apply {
            setLanguageCode("en") // Set language for auth emails
        }

        enableEdgeToEdge()
        setContent {
            GhorOnGoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    var startDestination by remember { mutableStateOf("login") }

                    // Use LaunchedEffect to handle auth state changes reactively
                    LaunchedEffect(Unit) {
                        auth.addAuthStateListener { firebaseAuth ->
                            val user = firebaseAuth.currentUser
                            startDestination = if (user != null && user.isEmailVerified) {
                                "home"
                            } else {
                                "login"
                            }
                        }

                        // Initial check
                        auth.currentUser?.let {
                            try {
                                it.reload().await() // Force refresh user data
                                startDestination = if (it.isEmailVerified) "home" else "login"
                            } catch (e: Exception) {
                                // Handle error if needed
                            }
                        }
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
        // Refresh user data when activity starts
        auth.currentUser?.reload()
    }
}