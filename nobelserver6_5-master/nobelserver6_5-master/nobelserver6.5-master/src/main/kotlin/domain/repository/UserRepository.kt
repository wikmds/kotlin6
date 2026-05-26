package com.example.domain.repository

import com.example.domain.model.User

interface UserRepository {
    suspend fun registerUser(username: String, passwordHash: String): User?
    suspend fun findByUsername(username: String): User?
    suspend fun checkPassword(username: String, password: String): Boolean
}