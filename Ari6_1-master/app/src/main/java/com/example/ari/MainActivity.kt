package com.example.ari

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ari.di.AppModule
import com.example.ari.navigation.AppNavigation // Импорт из нового пакета
import com.example.ari.presentation.PhotosViewModel
import com.example.ari.ui.theme.AriTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AriTheme {
                val viewModel: PhotosViewModel = viewModel(factory = AppModule.viewModelFactory)
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}