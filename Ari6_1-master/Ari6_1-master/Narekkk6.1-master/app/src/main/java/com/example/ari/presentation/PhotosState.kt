package com.example.ari.presentation

import com.example.ari.domain.Photo

sealed class PhotosState {
    object Loading : PhotosState()
    data class Success(val photos: List<Photo>) : PhotosState()
    data class Error(val message: String) : PhotosState()
}