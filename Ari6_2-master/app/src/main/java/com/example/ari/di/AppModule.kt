package com.example.ari.di

import com.example.ari.data.LaureateRepositoryImpl
import com.example.ari.data.NetworkModule
import com.example.ari.domain.GetLaureatesUseCase
import com.example.ari.presentation.LaureatesViewModelFactory

object AppModule {
    // Ktor клиент из NetworkModule
    private val client = NetworkModule.client

    // Репозиторий
    private val repository = LaureateRepositoryImpl(client)

    // UseCase для ViewModel
    val getLaureatesUseCase = GetLaureatesUseCase(repository)

    val viewModelFactory = LaureatesViewModelFactory(getLaureatesUseCase)
}
