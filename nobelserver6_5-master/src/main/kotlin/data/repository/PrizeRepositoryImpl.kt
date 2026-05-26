package com.example.data.repository

import com.example.data.database.LaureateTable
import com.example.data.database.PrizeTable
import com.example.data.database.UserPrizesTable
import com.example.data.dto.NobelResponse
import com.example.domain.model.Laureate
import com.example.domain.model.Prize
import com.example.domain.repository.PrizeRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime
import java.util.UUID

class PrizeRepositoryImpl(private val httpClient: HttpClient) : PrizeRepository {

    override suspend fun syncFromApi() {
        try {
            val response: NobelResponse = httpClient.get("https://api.nobelprize.org/2.1/nobelPrizes?limit=15").body()

            newSuspendedTransaction {
                response.nobelPrizes.forEach { prizeDto ->
                    val pYear = prizeDto.awardYear ?: "Unknown"
                    val pCat = prizeDto.category?.en ?: "Unknown"
                    val pId = "$pYear-$pCat"

                    if (PrizeTable.selectAll().where { PrizeTable.id eq pId }.empty()) {
                        PrizeTable.insert {
                            it[id] = pId
                            it[awardYear] = pYear
                            it[category] = pCat
                        }

                        prizeDto.laureates.forEach { lDto ->
                            val lId = lDto.id ?: UUID.randomUUID().toString()
                            val lName = lDto.knownName?.en ?: lDto.orgName?.en ?: "Unknown"

                            if (LaureateTable.selectAll().where { LaureateTable.id eq lId }.empty()) {
                                LaureateTable.insert {
                                    it[id] = lId
                                    it[prizeId] = pId
                                    it[fullName] = lName
                                    it[motivation] = lDto.motivation?.en
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("Ошибка синхронизации с API: ${e.message}")
        }
    }

    override suspend fun getAllPrizes(): List<Prize> = newSuspendedTransaction {
        PrizeTable.selectAll().map { row ->
            val pId = row[PrizeTable.id]
            val laureates = LaureateTable.selectAll().where { LaureateTable.prizeId eq pId }.map { lRow ->
                Laureate(lRow[LaureateTable.id], lRow[LaureateTable.fullName], lRow[LaureateTable.motivation])
            }
            Prize(pId, row[PrizeTable.awardYear], row[PrizeTable.category], laureates)
        }
    }

    override suspend fun getFavorites(userId: Int): List<Prize> = newSuspendedTransaction {
        val favoritePrizeIds = UserPrizesTable.selectAll().where { UserPrizesTable.userId eq userId }.map { it[UserPrizesTable.prizeId] }

        PrizeTable.selectAll().where { PrizeTable.id inList favoritePrizeIds }.map { row ->
            val pId = row[PrizeTable.id]
            val laureates = LaureateTable.selectAll().where { LaureateTable.prizeId eq pId }.map { lRow ->
                Laureate(lRow[LaureateTable.id], lRow[LaureateTable.fullName], lRow[LaureateTable.motivation])
            }
            Prize(pId, row[PrizeTable.awardYear], row[PrizeTable.category], laureates)
        }
    }

    override suspend fun addFavorite(userId: Int, prizeId: String): Boolean = newSuspendedTransaction {
        if (UserPrizesTable.selectAll().where { (UserPrizesTable.userId eq userId) and (UserPrizesTable.prizeId eq prizeId) }.empty()) {
            UserPrizesTable.insert {
                it[UserPrizesTable.userId] = userId
                it[UserPrizesTable.prizeId] = prizeId
                it[addedAt] = LocalDateTime.now()
            }
            true
        } else false
    }

    override suspend fun removeFavorite(userId: Int, prizeId: String): Boolean = newSuspendedTransaction {
        UserPrizesTable.deleteWhere { (UserPrizesTable.userId eq userId) and (UserPrizesTable.prizeId eq prizeId) } > 0
    }
}