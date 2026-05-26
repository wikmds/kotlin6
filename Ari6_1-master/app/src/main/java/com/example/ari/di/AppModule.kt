package com.example.ari.di

import com.example.ari.data.NetworkModule
import com.example.ari.data.PhotoRepositoryImpl
import com.example.ari.domain.GetPhotosUseCase
import com.example.ari.presentation.PhotosViewModelFactory

object AppModule {
    // Централизованное место для создания всех "связей"
    private val api = NetworkModule.api
    private val repository = PhotoRepositoryImpl(api)
    val getPhotosUseCase = GetPhotosUseCase(repository)

    val viewModelFactory = PhotosViewModelFactory(getPhotosUseCase)
}