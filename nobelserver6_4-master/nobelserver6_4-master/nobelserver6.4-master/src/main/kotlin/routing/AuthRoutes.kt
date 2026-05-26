package com.example.routing

import com.example.data.dto.LoginRequest
import com.example.data.dto.LoginResponse
import com.example.domain.repository.UserRepository
import com.example.plugins.JwtConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes(userRepository: UserRepository) {
    post("/auth/login") {
        val request = call.receive<LoginRequest>()

        if (userRepository.validateCredentials(request.username, request.password)) {
            val token = JwtConfig.generateToken(request.username)
            call.respond(LoginResponse(token))
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Неверный логин или пароль")
        }
    }
}