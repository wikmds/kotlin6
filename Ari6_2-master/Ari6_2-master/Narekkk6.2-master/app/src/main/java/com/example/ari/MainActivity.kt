package com.example.ari

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ari.di.AppModule
import com.example.ari.navigation.AppNavigation
import com.example.ari.presentation.LaureatesViewModel
import com.example.ari.ui.theme.AriTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AriTheme {
                val viewModel: LaureatesViewModel = viewModel(factory = AppModule.viewModelFactory)
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}