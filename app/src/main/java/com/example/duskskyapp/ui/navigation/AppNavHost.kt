package com.example.duskskyapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.duskskyapp.ui.auth.LoginScreen
import com.example.duskskyapp.ui.auth.RegisterScreen
import com.example.duskskyapp.ui.home.GameDetailScreen
import com.example.duskskyapp.ui.home.HomeScreen
import com.example.duskskyapp.ui.home.HomeViewModel
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

        composable("home") {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val popularGames by homeViewModel.popularGames.collectAsState(emptyList())

            HomeScreen(
                popularGames = popularGames,
                onGameClick  = { gameId ->
                    navController.navigate("game/$gameId")
                },
                onLogout     = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onOpenDrawer = {
                    // TODO: abrir tu drawer
                },
                onSearch     = {
                    // TODO: navegar a búsqueda
                },
                onFabClick   = {
                    // TODO: flujo “agregar juego”
                }
            )
        }

        // Nueva ruta para detalle de juego
        composable("game/{gameId}") { backStackEntry ->
            val gameId = backStackEntry.arguments!!.getString("gameId")!!
            GameDetailScreen(
                gameId = gameId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
