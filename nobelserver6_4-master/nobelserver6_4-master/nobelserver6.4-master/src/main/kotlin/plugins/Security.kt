package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.util.*

// Конфигурация JWT
object JwtConfig {
    const val secret = "my-super-secret-key-at-least-32-chars-long" // Ключ >= 32 символов
    const val issuer = "com.example.nobel"
    const val audience = "com.example.nobel"
    const val realm = "Access to prizes"

    fun generateToken(username: String): String = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("username", username)
        .withExpiresAt(Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30 минут
        .sign(Algorithm.HMAC256(secret))
}

fun Application.configureSecurity() {
    authentication {
        jwt("auth-jwt") {
            realm = JwtConfig.realm
            verifier(
                JWT.require(Algorithm.HMAC256(JwtConfig.secret))
                    .withAudience(JwtConfig.audience)
                    .withIssuer(JwtConfig.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(JwtConfig.audience)) {
                    JWTPrincipal(credential.payload)
                } else null
            }
            challenge { _, _ ->
                call.respond(io.ktor.http.HttpStatusCode.Unauthorized, "Токен недействителен или отсутствует")
            }
        }
    }
}