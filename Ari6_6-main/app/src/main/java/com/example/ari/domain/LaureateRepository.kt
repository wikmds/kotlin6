package com.example.ari.domain

interface LaureateRepository {
    suspend fun getLaureates(year: String?, category: String?): Result<List<Laureate>>
}