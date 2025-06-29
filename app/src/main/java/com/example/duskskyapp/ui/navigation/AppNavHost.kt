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
import com.example.duskskyapp.ui.detail.GameDetailScreen
import com.example.duskskyapp.ui.home.HomeScreen
import com.example.duskskyapp.ui.home.HomeViewModel
import com.example.duskskyapp.ui.lists.GameListDetailScreen
import com.example.duskskyapp.ui.lists.GameListsScreen
import com.example.duskskyapp.ui.profile.GameProfileScreen
import com.example.duskskyapp.ui.welcome.WelcomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "welcome") {

        composable("welcome") {
            WelcomeScreen(
                onRegisterClick = { navController.navigate("register") },
                onLoginClick = { navController.navigate("login") }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate("register") },
                onBack = {
                    navController.navigate("welcome") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("welcome") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onBack = {
                    navController.navigate("welcome") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val popularGames by homeViewModel.popularGames.collectAsState() // ✅ CORREGIDO

            HomeScreen(
                popularGames = popularGames,
                onGameClick = { gameId ->
                    navController.navigate("game/$gameId")
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onNavigateToLists = { userId ->
                    navController.navigate("lists/$userId")
                },
                onOpenDrawer = { /* TODO */ },
                onSearch = { /* TODO */ },
                onFabClick = { /* TODO */ }
            )
        }

        composable("game/{gameId}") { backStackEntry ->
            val gameId = backStackEntry.arguments!!.getString("gameId")!!
            GameDetailScreen(
                gameId = gameId,
                onBack = { navController.popBackStack() }
            )
        }

        composable("gameProfile/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: return@composable
            GameProfileScreen(
                userId = userId,
                onGameClick = { gameId ->
                    navController.navigate("game/$gameId")
                }
            )
        }

        composable("lists/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: return@composable
            GameListsScreen(
                userId = userId,
                onGameClick = { gameId ->
                    navController.navigate("game/$gameId")
                },
                onListClick = { listId ->
                    navController.navigate("lists/detail/$listId")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("lists/detail/{listId}") { backStackEntry ->
            val listId = backStackEntry.arguments?.getString("listId") ?: return@composable
            GameListDetailScreen(
                listId = listId,
                onGameClick = { gameId ->
                    navController.navigate("game/$gameId")
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
