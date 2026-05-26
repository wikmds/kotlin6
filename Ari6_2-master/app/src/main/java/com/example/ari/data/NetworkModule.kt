package com.example.ari.data

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object NetworkModule {
    val client = HttpClient(Android) {
        // Установка сериализатора JSON
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // Игнорируем лишние поля из API
                isLenient = true
            })
        }
        // Логирование запросов
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }
}