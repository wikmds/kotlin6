package com.example.ari.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    id: Int,
    usersViewModel: UsersViewModel,
    authViewModel: AuthViewModel,
    onBackClick: () -> Unit
) {
    val state by usersViewModel.detailState.collectAsState()

    LaunchedEffect(id) {
        usersViewModel.loadUserDetail(id) // Грузим детали по id
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, contentDescription = "Назад") }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (state) {
                is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is UiState.Error -> {
                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Ошибка: ${(state as UiState.Error).message}", color = MaterialTheme.colorScheme.error)
                        Button(onClick = { usersViewModel.loadUserDetail(id) }, modifier = Modifier.padding(top = 8.dp)) {
                            Text("Повторить")
                        }
                    }
                }
                is UiState.Success -> {
                    val user = (state as UiState.Success).data
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = user.image,
                            contentDescription = "Avatar",
                            modifier = Modifier.size(150.dp).clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(user.fullName, style = MaterialTheme.typography.headlineMedium)
                        Text(user.email, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

                        Spacer(modifier = Modifier.weight(1f)) // Выталкивает кнопку выхода вниз

                        Button(
                            onClick = { authViewModel.logout() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Выйти")
                        }
                    }
                }
                else -> {}
            }
        }
    }
}