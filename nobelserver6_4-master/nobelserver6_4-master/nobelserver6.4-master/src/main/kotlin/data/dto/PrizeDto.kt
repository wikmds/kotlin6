package com.example.data.dto

import com.example.domain.models.Laureate
import com.example.domain.models.NobelPrize
import kotlinx.serialization.Serializable

@Serializable
data class LaureateDto(val id: String, val name: String, val motivation: String)

@Serializable
data class NobelPrizeDto(val awardYear: String, val category: String, val laureates: List<LaureateDto>)

// Функции-мапперы, чтобы превращать чистые модели в JSON-модели
fun NobelPrize.toDto() = NobelPrizeDto(
    awardYear = awardYear,
    category = category,
    laureates = laureates.map { LaureateDto(it.id, it.name, it.motivation) }
)