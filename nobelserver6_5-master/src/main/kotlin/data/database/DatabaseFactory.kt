package com.example.data.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val config = HikariConfig().apply {
            // Твои доступы от Neon.tech
            jdbcUrl = "jdbc:postgresql://ep-shiny-bird-alwknvi8.c-3.eu-central-1.aws.neon.tech/neondb?sslmode=require"
            driverClassName = "org.postgresql.Driver"
            username = "neondb_owner"
            password = "npg_5BLaU6jpNDEH"

            // Настройки пула
            maximumPoolSize = 10
            minimumIdle = 2
            idleTimeout = 300000
            maxLifetime = 1800000
            connectionTimeout = 30000
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        // Автоматически создаем таблицы в БД при запуске
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                UserTable, PrizeTable, LaureateTable, UserPrizesTable
            )
        }
    }
}