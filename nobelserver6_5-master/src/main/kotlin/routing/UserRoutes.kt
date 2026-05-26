package com.example.routing

import com.example.domain.repository.UserRepository
import com.example.domain.usecase.PrizeUseCases
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(userRepository: UserRepository, prizeUseCases: PrizeUseCases) {
    // Защищаем все маршруты токеном
    authenticate("auth-jwt") {
        route("/users/me") {
            // Профиль пользователя
            get {
                val username = call.principal<JWTPrincipal>()?.payload?.getClaim("username")?.asString()
                val user = username?.let { userRepository.findByUsername(it) }
                if (user != null) call.respond(user) else call.respond(HttpStatusCode.Unauthorized)
            }

            // Избранные премии
            get("/prizes") {
                val username = call.principal<JWTPrincipal>()?.payload?.getClaim("username")?.asString()
                val user = username?.let { userRepository.findByUsername(it) } ?: return@get call.respond(HttpStatusCode.Unauthorized)

                call.respond(prizeUseCases.getFavorites(user.id))
            }

            // Добавить премию в избранное
            post("/prizes/{prizeId}") {
                val prizeId = call.parameters["prizeId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val username = call.principal<JWTPrincipal>()?.payload?.getClaim("username")?.asString()
                val user = username?.let { userRepository.findByUsername(it) } ?: return@post call.respond(HttpStatusCode.Unauthorized)

                if (prizeUseCases.addFavorite(user.id, prizeId)) call.respond(HttpStatusCode.OK, "Добавлено в избранное")
                else call.respond(HttpStatusCode.Conflict, "Уже в избранном")
            }

            // Удалить премию из избранного
            delete("/prizes/{prizeId}") {
                val prizeId = call.parameters["prizeId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val username = call.principal<JWTPrincipal>()?.payload?.getClaim("username")?.asString()
                val user = username?.let { userRepository.findByUsername(it) } ?: return@delete call.respond(HttpStatusCode.Unauthorized)

                if (prizeUseCases.removeFavorite(user.id, prizeId)) call.respond(HttpStatusCode.OK, "Удалено из избранного")
                else call.respond(HttpStatusCode.NotFound, "Не найдено в избранном")
            }
        }
    }
}