package com.example.routing

import com.example.data.dto.LoginRequest
import com.example.data.dto.LoginResponse
import com.example.domain.usecase.AuthUseCases
import com.example.security.PasswordHasher
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes(authUseCases: AuthUseCases) {
    // Логин
    post("/login") {
        val req = call.receive<LoginRequest>()
        val token = authUseCases.login(req.username, req.password)
        if (token != null) call.respond(LoginResponse(token))
        else call.respond(HttpStatusCode.Unauthorized, "Неверный логин или пароль")
    }

    // Регистрация
    post("/register") {
        val req = call.receive<LoginRequest>()
        val hash = PasswordHasher.hash(req.password)
        val user = authUseCases.register(req.username, hash)
        if (user != null) call.respond(HttpStatusCode.Created, "Пользователь создан")
        else call.respond(HttpStatusCode.Conflict, "Пользователь уже существует")
    }
}