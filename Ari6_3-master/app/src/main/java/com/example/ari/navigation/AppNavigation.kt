package com.example.ari.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ari.presentation.AuthViewModel
import com.example.ari.presentation.LoginScreen
import com.example.ari.presentation.UserDetailScreen
import com.example.ari.presentation.UsersListScreen
import com.example.ari.presentation.UsersViewModel

@Composable
fun AppNavigation(
    hasToken: Boolean,
    authViewModel: AuthViewModel,
    usersViewModel: UsersViewModel
) {
    val navController = rememberNavController()

    //Автоматическая навигация при изменении токена
    LaunchedEffect(hasToken) {
        if (hasToken) {
            navController.navigate("users") {
                popUpTo(0) // Очищаем историю, чтобы нельзя было вернуться назад кнопкой телефона
            }
        } else {
            navController.navigate("login") {
                popUpTo(0)
            }
        }
    }

    NavHost(navController = navController, startDestination = if (hasToken) "users" else "login") {
        composable("login") {
            LoginScreen(viewModel = authViewModel)
        }

        composable("users") {
            UsersListScreen(
                viewModel = usersViewModel,
                onUserClick = { id -> navController.navigate("user_detail/$id") }
            )
        }

        composable("user_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            if (id != null) {
                UserDetailScreen(
                    id = id,
                    usersViewModel = usersViewModel,
                    authViewModel = authViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}