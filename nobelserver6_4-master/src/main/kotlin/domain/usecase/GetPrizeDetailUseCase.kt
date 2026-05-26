package com.example.domain.usecase

import com.example.domain.models.NobelPrize
import com.example.domain.repository.PrizeRepository

class GetPrizeDetailUseCase(private val repository: PrizeRepository) {
    fun execute(year: String, category: String): NobelPrize? {
        return repository.getPrize(year, category)
    }
}