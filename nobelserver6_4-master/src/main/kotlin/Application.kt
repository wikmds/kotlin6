package com.example

import com.example.data.repository.PrizeRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.usecase.GetPrizeDetailUseCase
import com.example.domain.usecase.GetPrizeLaureatesUseCase
import com.example.domain.usecase.GetPrizesUseCase
import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    // Инициализируем репозитории и UseCases
    val prizeRepository = PrizeRepositoryImpl()
    val userRepository = UserRepositoryImpl()

    val getPrizesUseCase = GetPrizesUseCase(prizeRepository)
    val getDetailUseCase = GetPrizeDetailUseCase(prizeRepository)
    val getLaureatesUseCase = GetPrizeLaureatesUseCase(prizeRepository)

    //  Подключаем плагины
    configureSecurity()       // JWT
    configureSerialization()  // JSON
    configureMonitoring()     // Логирование

    // Настраиваем маршруты
    configureRouting(
        userRepository,
        getPrizesUseCase,
        getDetailUseCase,
        getLaureatesUseCase
    )
}