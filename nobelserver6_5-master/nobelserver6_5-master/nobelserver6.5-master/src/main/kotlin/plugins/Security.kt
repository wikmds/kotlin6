package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.util.*

object JwtConfig {
    const val secret = "my-super-secret-key-at-least-32-chars-long"
    const val issuer = "com.example.nobel"
    const val audience = "com.example.nobel"
    const val realm = "Access to prizes"

    fun generateToken(username: String, role: String): String = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("username", username)
        .withClaim("role", role)
        .withExpiresAt(Date(System.currentTimeMillis() + 30 * 60 * 1000))
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