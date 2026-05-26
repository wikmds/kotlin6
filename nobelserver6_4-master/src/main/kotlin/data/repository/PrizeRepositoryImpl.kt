package com.example.data.repository

import com.example.domain.models.Laureate
import com.example.domain.models.NobelPrize
import com.example.domain.repository.PrizeRepository

class PrizeRepositoryImpl : PrizeRepository {
    private val prizes = listOf(
        NobelPrize("2023", "physics", listOf(
            Laureate("1", "Pierre Agostini", "experimental methods that generate attosecond pulses"),
            Laureate("2", "Ferenc Krausz", "experimental methods that generate attosecond pulses")
        )),
        NobelPrize("2023", "peace", listOf(
            Laureate("3", "Narges Mohammadi", "fight against the oppression of women in Iran")
        )),
        NobelPrize("2022", "literature", listOf(
            Laureate("4", "Annie Ernaux", "for the courage and clinical acuity")
        ))
    )

    override fun getAllPrizes(): List<NobelPrize> = prizes

    override fun getPrize(year: String, category: String): NobelPrize? {
        return prizes.find { it.awardYear == year && it.category == category }
    }
}
