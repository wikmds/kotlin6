package com.example.ari.data

import com.example.ari.domain.Laureate
import com.example.ari.domain.LaureateRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class LaureateRepositoryImpl(
    private val client: HttpClient
) : LaureateRepository {

    override suspend fun getLaureates(year: String?, category: String?): Result<List<Laureate>> {
        return try {
            // Запрашиваем данные с эндпоинта /prizes твоего сервера
            val response: List<PrizeDto> = client.get("prizes").body()

            val allLaureates = mutableListOf<Laureate>()

            // Твой сервер присылает список премий, внутри каждой - список лауреатов
            for (prize in response) {
                for (laureateDto in prize.laureates) {
                    allLaureates.add(
                        Laureate(
                            id = laureateDto.id,
                            awardYear = prize.awardYear,
                            category = prize.category,
                            fullName = laureateDto.fullName,
                            motivation = laureateDto.motivation ?: "No motivation provided",
                            birthCountry = "See details" // В нашем упрощенном API страны нет
                        )
                    )
                }
            }

            // Применяем фильтры вручную (так как наш бэкенд отдает всё сразу)
            val filteredList = allLaureates.filter { laureate ->
                val yearMatch = year == null || year.isBlank() || laureate.awardYear == year
                val categoryMatch = category == null || category == "All" ||
                        laureate.category.equals(category, ignoreCase = true)
                yearMatch && categoryMatch
            }

            Result.success(filteredList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}