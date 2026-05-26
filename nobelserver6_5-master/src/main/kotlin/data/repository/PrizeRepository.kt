package com.example.domain.repository

import com.example.domain.model.Prize

interface PrizeRepository {
    suspend fun syncFromApi() // Скачивает из интернета и кладет в БД
    suspend fun getAllPrizes(): List<Prize> // Достает из нашей БД

    // Для избранных премий (Требование из методички)
    suspend fun getFavorites(userId: Int): List<Prize>
    suspend fun addFavorite(userId: Int, prizeId: String): Boolean
    suspend fun removeFavorite(userId: Int, prizeId: String): Boolean
}