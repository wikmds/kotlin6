package com.example.routing

import com.example.domain.usecase.PrizeUseCases
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.prizeRoutes(prizeUseCases: PrizeUseCases) {
    get("/prizes") {
        // Список премий (кэш в БД + обновление из API)
        val prizes = prizeUseCases.getAndSyncPrizes()
        call.respond(prizes)
    }
}