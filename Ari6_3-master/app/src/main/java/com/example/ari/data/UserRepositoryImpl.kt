package com.example.ari.data

import com.example.ari.domain.User
import com.example.ari.domain.UserRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class UserRepositoryImpl(
    private val client: HttpClient
) : UserRepository {

    override suspend fun getUsers(): Result<List<User>> {
        return try {
            // Auth токен  автоматически
            val response: UsersResponse = client.get("users").body()
            Result.success(response.users.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserById(id: Int): Result<User> {
        return try {
            val response: UserDto = client.get("users/$id").body()
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}