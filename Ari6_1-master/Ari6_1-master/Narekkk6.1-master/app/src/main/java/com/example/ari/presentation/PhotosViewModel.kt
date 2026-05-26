package com.example.ari.presentation

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ari.data.NetworkModule
import com.example.ari.domain.GetPhotosUseCase
import com.example.ari.domain.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Request
import java.io.IOException

class PhotosViewModel(
    private val getPhotosUseCase: GetPhotosUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<PhotosState>(PhotosState.Loading)
    val state: StateFlow<PhotosState> = _state.asStateFlow()

    init {
        loadPhotos()
    }

    fun loadPhotos() {
        viewModelScope.launch {
            _state.value = PhotosState.Loading
            val result = getPhotosUseCase()
            result.fold(
                onSuccess = { photos ->
                    _state.value = PhotosState.Success(photos)
                },
                onFailure = { error ->
                    _state.value = PhotosState.Error(error.localizedMessage ?: "Неизвестная ошибка")
                }
            )
        }
    }

    // Поиск фото по ID для экрана деталей
    fun getPhotoById(id: String): Photo? {
        val currentState = _state.value
        if (currentState is PhotosState.Success) {
            return currentState.photos.find { it.id == id }
        }
        return null
    }

    // Логика скачивания через SAF в указанный Uri
    fun downloadAndSavePhoto(context: Context, photoUrl: String, uri: Uri) {
        Toast.makeText(context, "Началось скачивание...", Toast.LENGTH_SHORT).show()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(photoUrl).build()
                NetworkModule.client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Не удалось скачать")

                    val inputStream = response.body?.byteStream()
                    val outputStream = context.contentResolver.openOutputStream(uri)

                    if (inputStream != null && outputStream != null) {
                        inputStream.copyTo(outputStream)
                        outputStream.close()
                        inputStream.close()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Фото успешно сохранено", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Теперь тост покажет точную причину, почему не удалось скачать!
                    Toast.makeText(context, "Ошибка: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

// Фабрика для ручного создания ViewModel
class PhotosViewModelFactory(
    private val getPhotosUseCase: GetPhotosUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PhotosViewModel(getPhotosUseCase) as T
    }
}