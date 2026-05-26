package com.example.ari.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ari.presentation.LaureateDetailScreen
import com.example.ari.presentation.LaureateListScreen
import com.example.ari.presentation.LaureatesViewModel

@Composable
fun AppNavigation(viewModel: LaureatesViewModel) {
    val navController = rememberNavController()

    // Подписываемся на состояния
    val state by viewModel.state.collectAsState()
    val yearFilter by viewModel.currentYearFilter.collectAsState()
    val categoryFilter by viewModel.currentCategoryFilter.collectAsState()

    NavHost(navController = navController, startDestination = "laureate_list") {
        composable("laureate_list") {
            LaureateListScreen(
                state = state,
                yearFilter = yearFilter,
                categoryFilter = categoryFilter,
                onYearChanged = { viewModel.updateYearFilter(it) },
                onCategoryChanged = { viewModel.updateCategoryFilter(it) },
                onApplyFilters = { viewModel.loadLaureates() },
                onLaureateClick = { id ->
                    navController.navigate("laureate_detail/$id")
                }
            )
        }

        composable("laureate_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val laureate = id?.let { viewModel.getLaureateById(it) }

            if (laureate != null) {
                LaureateDetailScreen(
                    laureate = laureate,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}