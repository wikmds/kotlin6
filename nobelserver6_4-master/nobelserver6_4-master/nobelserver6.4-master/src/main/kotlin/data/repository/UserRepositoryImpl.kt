package com.example.data.repository

import com.example.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    override fun validateCredentials(username: String, password: String): Boolean {
        // Простая проверка для теста
        return username == "admin" && password == "admin"
    }
}