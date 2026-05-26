package com.example.domain.models

data class NobelPrize(
    val awardYear: String,
    val category: String,
    val laureates: List<Laureate>
)