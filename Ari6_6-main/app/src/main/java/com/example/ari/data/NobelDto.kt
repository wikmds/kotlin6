package com.example.ari.data

import kotlinx.serialization.Serializable

// Модели для получения данных с ТВОЕГО сервера
@Serializable
data class PrizeDto(
    val id: String,
    val awardYear: String,
    val category: String,
    val laureates: List<LaureateDto> = emptyList()
)

@Serializable
data class LaureateDto(
    val id: String,
    val fullName: String,
    val motivation: String? = null
)