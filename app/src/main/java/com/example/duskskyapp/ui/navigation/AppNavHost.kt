package com.example.duskskyapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.duskskyapp.ui.auth.LoginScreen
import com.example.duskskyapp.ui.auth.RegisterScreen
import com.example.duskskyapp.ui.home.HomeScreen
import com.example.duskskyapp.ui.welcome.WelcomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "welcome") {

        // Ruta de Bienvenida
        composable("welcome") {
            WelcomeScreen(
                onRegisterClick = { navController.navigate("register") },
                onLoginClick    = { navController.navigate("login") }
            )
        }

        // Ruta de Login
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onBack = {
                    navController.navigate("welcome") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }


        // Ruta de Registro
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    // cuando termines de registrarte, vas a Home
                    navController.navigate("welcome") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    // “¿Ya tienes cuenta?” → Login
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onBack = {
                    // flecha de volver → Home
                    navController.navigate("welcome") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        // Ruta de Home (pantalla principal)
        composable("home") {
            HomeScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}
