package com.example.domain.repository

import com.example.domain.models.NobelPrize

interface PrizeRepository {
    fun getAllPrizes(): List<NobelPrize>
    fun getPrize(year: String, category: String): NobelPrize?
}