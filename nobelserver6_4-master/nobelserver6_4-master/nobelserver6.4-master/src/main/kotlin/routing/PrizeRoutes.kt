package com.example.routing

import com.example.data.dto.LaureateDto
import com.example.data.dto.toDto
import com.example.domain.usecase.GetPrizeDetailUseCase
import com.example.domain.usecase.GetPrizeLaureatesUseCase
import com.example.domain.usecase.GetPrizesUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.prizeRoutes(
    getPrizesUseCase: GetPrizesUseCase,
    getDetailUseCase: GetPrizeDetailUseCase,
    getLaureatesUseCase: GetPrizeLaureatesUseCase
) {

    authenticate("auth-jwt") {

    get("/prizes") {
        val prizes = getPrizesUseCase.execute()
        call.respond(prizes.map { it.toDto() })
    }

    get("/prizes/{year}/{category}") {
        val year = call.parameters["year"] ?: return@get call.respond(HttpStatusCode.BadRequest)
        val category = call.parameters["category"] ?: return@get call.respond(HttpStatusCode.BadRequest)

        val prize = getDetailUseCase.execute(year, category)
        if (prize != null) call.respond(prize.toDto())
        else call.respond(HttpStatusCode.NotFound, "Премия не найдена")
    }

    get("/prizes/{year}/{category}/laureates") {
        val year = call.parameters["year"] ?: return@get call.respond(HttpStatusCode.BadRequest)
        val category = call.parameters["category"] ?: return@get call.respond(HttpStatusCode.BadRequest)

        val laureates = getLaureatesUseCase.execute(year, category)
        if (laureates != null) {
            // превращаем доменный Laureate в LaureateDto
            call.respond(laureates.map { LaureateDto(it.id, it.name, it.motivation) })
        } else {
            call.respond(HttpStatusCode.NotFound, "Лауреаты не найдены")
        }
    }
    }
//}
