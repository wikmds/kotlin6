package com.example.ari.domain

class GetLaureatesUseCase(private val repository: LaureateRepository) {
    suspend operator fun invoke(year: String? = null, category: String? = null): Result<List<Laureate>> {
        return repository.getLaureates(year, category)
    }
}