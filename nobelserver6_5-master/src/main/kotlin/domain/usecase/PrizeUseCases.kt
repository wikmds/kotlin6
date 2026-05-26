package com.example.domain.usecase

import com.example.domain.model.Prize
import com.example.domain.repository.PrizeRepository

class PrizeUseCases(private val repository: PrizeRepository) {

    // Получить премии: сначала скачиваем новые из API, потом отдаем из нашей БД
    suspend fun getAndSyncPrizes(): List<Prize> {
        repository.syncFromApi()
        return repository.getAllPrizes()
    }

    suspend fun getFavorites(userId: Int) = repository.getFavorites(userId)

    suspend fun addFavorite(userId: Int, prizeId: String) = repository.addFavorite(userId, prizeId)

    suspend fun removeFavorite(userId: Int, prizeId: String) = repository.removeFavorite(userId, prizeId)
}