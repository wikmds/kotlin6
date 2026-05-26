package com.example.plugins

import com.example.domain.repository.UserRepository
import com.example.domain.usecase.GetPrizeDetailUseCase
import com.example.domain.usecase.GetPrizeLaureatesUseCase
import com.example.domain.usecase.GetPrizesUseCase
import com.example.routing.authRoutes
import com.example.routing.prizeRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userRepository: UserRepository,
    getPrizesUseCase: GetPrizesUseCase,
    getDetailUseCase: GetPrizeDetailUseCase,
    getLaureatesUseCase: GetPrizeLaureatesUseCase
) {
    routing {
        authRoutes(userRepository)
        prizeRoutes(getPrizesUseCase, getDetailUseCase, getLaureatesUseCase)
    }
}