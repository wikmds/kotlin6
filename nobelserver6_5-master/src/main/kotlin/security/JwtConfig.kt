package com.example.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
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
        .withExpiresAt(Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30 минут
        .sign(Algorithm.HMAC256(secret))
}