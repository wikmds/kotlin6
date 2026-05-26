package com.example.ari.data

import com.example.ari.domain.Laureate
import com.example.ari.domain.LaureateRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.util.UUID

class LaureateRepositoryImpl(
    private val client: HttpClient
) : LaureateRepository {

    override suspend fun getLaureates(year: String?, category: String?): Result<List<Laureate>> {
        return try {
            val response: NobelResponse = client.get("https://api.nobelprize.org/2.1/nobelPrizes") {
                url {
                    parameters.append("limit", "25")
                    parameters.append("offset", "0")

                    // Добавляем параметры фильтрации, если они выбраны
                    if (!year.isNullOrBlank()) {
                        parameters.append("nobelPrizeYear", year)
                    }
                    if (!category.isNullOrBlank() && category != "All") {
                        // API ожидает категории на английском в нижнем регистре (physics, peace и т.д.)
                        parameters.append("nobelPrizeCategory", category.lowercase())
                    }
                }
            }.body()

            val laureates = mutableListOf<Laureate>()

            // Проходим по премиям и вытаскиваем лауреатов
            for (prize in response.nobelPrizes) {
                val prizeYear = prize.awardYear ?: "N/A"
                val prizeCategory = prize.category?.en ?: "N/A"

                for (laureate in prize.laureates) {
                    val name = laureate.knownName?.en ?: laureate.orgName?.en ?: "Unknown"
                    val motivation = laureate.motivation?.en ?: "No motivation provided"
                    val country = laureate.birth?.place?.country?.en ?: "Unknown"

                    laureates.add(
                        Laureate(
                            id = laureate.id ?: UUID.randomUUID().toString(),
                            awardYear = prizeYear,
                            category = prizeCategory,
                            fullName = name,
                            motivation = motivation,
                            birthCountry = country
                        )
                    )
                }
            }

            Result.success(laureates)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}