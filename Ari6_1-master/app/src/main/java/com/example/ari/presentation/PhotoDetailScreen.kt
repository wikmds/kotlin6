package com.example.ari.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ari.domain.Photo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailScreen(
    photo: Photo,
    onBackClick: () -> Unit,
    viewModel: PhotosViewModel
) {
    val context = LocalContext.current

    // SAF (Storage Access Framework) лаунчер для создания файла в Downloads
    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("image/jpeg")
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.downloadAndSavePhoto(context, photo.downloadUrl, uri)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали фото") },
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
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = photo.downloadUrl, // Большое оригинальное фото
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text("Автор: ${photo.author}", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Разрешение: ${photo.width} × ${photo.height}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Ссылка API: ${photo.url}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        // Запускаем системное окно выбора сохранения
                        createDocumentLauncher.launch("photo_${photo.id}.jpg")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Скачать фото")
                }
            }
        }
    }
}