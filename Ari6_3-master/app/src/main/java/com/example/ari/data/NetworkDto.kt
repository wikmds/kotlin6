package com.example.ari.data

import com.example.ari.domain.User
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
    val expiresInMins: Int = 60 // DummyJson требует указать срок жизни токена
)

@Serializable
data class LoginResponse(
    val accessToken: String // Нам нужен только токен из ответа
)

@Serializable
data class UsersResponse(
    val users: List<UserDto>
)

@Serializable
data class UserDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val image: String
)

// Маппер DTO -> Domain
fun UserDto.toDomain() = User(
    id = id,
    firstName = firstName,
    lastName = lastName,
    username = username,
    email = email,
    image = image
)