package com.example.domain.usecase

import com.example.domain.models.NobelPrize
import com.example.domain.repository.PrizeRepository

class GetPrizesUseCase(private val repository: PrizeRepository) {
    fun execute(): List<NobelPrize> {
        return repository.getAllPrizes()
    }
}