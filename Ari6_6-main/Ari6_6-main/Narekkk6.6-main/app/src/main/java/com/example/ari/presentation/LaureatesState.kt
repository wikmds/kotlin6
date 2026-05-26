package com.example.ari.presentation

import com.example.ari.domain.Laureate

sealed class LaureatesState {
    object Loading : LaureatesState()
    data class Success(val laureates: List<Laureate>) : LaureatesState()
    data class Error(val message: String) : LaureatesState()
}