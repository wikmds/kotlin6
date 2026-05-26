package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Laureate(
    val id: String,
    val fullName: String,
    val motivation: String?
)

@Serializable
data class Prize(
    val id: String,
    val awardYear: String,
    val category: String,
    val laureates: List<Laureate> = emptyList()
)