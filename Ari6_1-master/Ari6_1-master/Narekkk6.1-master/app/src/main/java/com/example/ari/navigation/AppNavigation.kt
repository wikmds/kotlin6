package com.example.ari.navigation // Теперь пакет такой

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ari.presentation.PhotoDetailScreen
import com.example.ari.presentation.PhotoListScreen
import com.example.ari.presentation.PhotosViewModel

@Composable
fun AppNavigation(viewModel: PhotosViewModel) {
    val navController = rememberNavController()
    val state = viewModel.state.collectAsState().value

    NavHost(navController = navController, startDestination = "photo_list") {
        composable("photo_list") {
            PhotoListScreen(
                state = state,
                onRetry = { viewModel.loadPhotos() },
                onPhotoClick = { photoId ->
                    navController.navigate("photo_detail/$photoId")
                }
            )
        }

        composable("photo_detail/{photoId}") { backStackEntry ->
            val photoId = backStackEntry.arguments?.getString("photoId")
            val photo = photoId?.let { viewModel.getPhotoById(it) }

            if (photo != null) {
                PhotoDetailScreen(
                    photo = photo,
                    onBackClick = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }
        }
    }
}