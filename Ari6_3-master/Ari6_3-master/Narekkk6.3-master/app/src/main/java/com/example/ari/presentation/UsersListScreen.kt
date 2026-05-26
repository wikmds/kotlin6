package com.example.ari.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ari.domain.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersListScreen(
    viewModel: UsersViewModel,
    onUserClick: (Int) -> Unit
) {
    val state by viewModel.usersState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUsers() // Загружаем список при старте
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Список пользователей") }) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (state) {
                is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is UiState.Error -> {
                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Ошибка: ${(state as UiState.Error).message}", color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.loadUsers() }, modifier = Modifier.padding(top = 8.dp)) {
                            Text("Повторить")
                        }
                    }
                }
                is UiState.Success -> {
                    val users = (state as UiState.Success<List<User>>).data
                    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(users) { user ->
                            Card(onClick = { onUserClick(user.id) }, modifier = Modifier.fillMaxWidth()) {
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    AsyncImage(
                                        model = user.image,
                                        contentDescription = "Avatar",
                                        modifier = Modifier.size(50.dp).clip(CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(user.fullName, style = MaterialTheme.typography.titleMedium)
                                        Text("@${user.username} | ${user.email}", style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}