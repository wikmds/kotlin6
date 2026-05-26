package com.example.ari.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ari.domain.Laureate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaureateDetailScreen(
    laureate: Laureate,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = laureate.photoUrl,
                contentDescription = laureate.fullName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = laureate.fullName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(text = "${laureate.awardYear} | ${laureate.category}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            Spacer(modifier = Modifier.height(24.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Страна рождения:", fontWeight = FontWeight.Bold)
                    Text(laureate.birthCountry)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Обоснование (Motivation):", fontWeight = FontWeight.Bold)
                    Text(laureate.motivation)
                }
            }
        }
    }
}