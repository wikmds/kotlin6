package com.example.domain.repository

// Интерфейс для проверки логина и пароля
interface UserRepository {
    fun validateCredentials(username: String, password: String): Boolean
}