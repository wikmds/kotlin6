package com.example.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class NobelResponse(val nobelPrizes: List<PrizeDto> = emptyList())

@Serializable
data class PrizeDto(
    val awardYear: String? = null,
    val category: TranslatedString? = null,
    val laureates: List<LaureateDto> = emptyList()
)

@Serializable
data class LaureateDto(
    val id: String? = null,
    val knownName: TranslatedString? = null,
    val orgName: TranslatedString? = null,
    val motivation: TranslatedString? = null
)

@Serializable
data class TranslatedString(val en: String? = null)