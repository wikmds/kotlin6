package com.example.ari.domain

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Unit>
    suspend fun logout()
    fun getAuthToken(): Flow<String?> // авторизованы мы или нет
}