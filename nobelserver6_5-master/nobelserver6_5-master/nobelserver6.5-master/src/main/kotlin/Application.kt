package com.example

import com.example.data.database.DatabaseFactory
import com.example.data.repository.PrizeRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.usecase.AuthUseCases
import com.example.domain.usecase.PrizeUseCases
import com.example.plugins.*
import com.example.routing.authRoutes
import com.example.routing.prizeRoutes
import com.example.routing.userRoutes
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    // 1. Инициализация Базы Данных (создание таблиц в Neon)
    DatabaseFactory.init()


    // 2. HTTP-клиент для скачивания премий
    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true //игнорировать лишние языки и поля
                isLenient = true
            })
        }
    }

    // 3. Создание репозиториев и UseCases
    val userRepository = UserRepositoryImpl()
    val prizeRepository = PrizeRepositoryImpl(httpClient)

    val authUseCases = AuthUseCases(userRepository)
    val prizeUseCases = PrizeUseCases(prizeRepository)

    // 4. Подключение плагинов
    configureSecurity() // JWT из файла Security.kt
    configureSwagger()  // Документация из файла Swagger.kt

    install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
        json() // Работа с JSON
    }
    install(io.ktor.server.plugins.calllogging.CallLogging) // Логирование запросов

    // 5. Подключение всех маршрутов
    routing {
        authRoutes(authUseCases)
        prizeRoutes(prizeUseCases)
        userRoutes(userRepository, prizeUseCases)
    }
}