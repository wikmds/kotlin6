package com.example.domain.usecase

import com.example.domain.models.Laureate
import com.example.domain.repository.PrizeRepository

class GetPrizeLaureatesUseCase(private val repository: PrizeRepository) {
    fun execute(year: String, category: String): List<Laureate>? {
        val prize = repository.getPrize(year, category)
        return prize?.laureates
    }
}