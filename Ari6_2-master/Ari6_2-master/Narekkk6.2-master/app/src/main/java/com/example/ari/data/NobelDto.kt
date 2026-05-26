package com.example.ari.data

import kotlinx.serialization.Serializable

@Serializable
data class NobelResponse(
    val nobelPrizes: List<PrizeDto> = emptyList()
)

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
    val orgName: TranslatedString? = null, // Иногда премию дают организациям
    val motivation: TranslatedString? = null,
    val birth: BirthDto? = null
)

@Serializable
data class BirthDto(
    val place: PlaceDto? = null
)

@Serializable
data class PlaceDto(
    val country: TranslatedString? = null
)

@Serializable
data class TranslatedString(
    val en: String? = null
)