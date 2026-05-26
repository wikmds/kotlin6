package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.security.JwtConfig

class AuthUseCases(private val userRepository: UserRepository) {
    // Вход и генерация токена
    suspend fun login(username: String, pass: String): String? {
        if (userRepository.checkPassword(username, pass)) {
            val user = userRepository.findByUsername(username)
            return JwtConfig.generateToken(username, user?.role ?: "user")
        }
        return null
    }

    // Регистрация нового пользователя
    suspend fun register(username: String, passHash: String): User? {
        return userRepository.registerUser(username, passHash)
    }
}