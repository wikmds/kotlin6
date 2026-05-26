package com.example.ari.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ari.data.AuthRepositoryImpl
import com.example.ari.data.NetworkModule
import com.example.ari.data.TokenManager
import com.example.ari.data.UserRepositoryImpl
import com.example.ari.domain.AuthRepository
import com.example.ari.domain.UserRepository
import com.example.ari.presentation.AuthViewModel
import com.example.ari.presentation.UsersViewModel

object AppModule {
    lateinit var authRepository: AuthRepository
    lateinit var userRepository: UserRepository

    // Вызовем этот метод из MainActivity, чтобы передать Context
    fun init(context: Context) {
        val tokenManager = TokenManager(context)
        val client = NetworkModule.createClient(tokenManager)

        authRepository = AuthRepositoryImpl(client, tokenManager)
        userRepository = UserRepositoryImpl(client)
    }

    // Универсальная фабрика для ViewModel
    val viewModelFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                return AuthViewModel(authRepository) as T
            }
            if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
                return UsersViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}