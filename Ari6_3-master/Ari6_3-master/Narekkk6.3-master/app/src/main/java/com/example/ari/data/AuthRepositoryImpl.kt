package com.example.ari.data

import android.os.Parcel
import android.os.Parcelable
import com.example.ari.domain.AuthRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val client: HttpClient,
    private val tokenManager: TokenManager
) : AuthRepository, Parcelable {

    constructor(parcel: Parcel) : this(
        TODO("client"),
        TODO("tokenManager")
    )

    override suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            val response: LoginResponse = client.post("auth/login") {
                contentType(ContentType.Application.Json) // Явно указываем, что JSON
                setBody(LoginRequest(username, password))
            }.body()

            // Сохраняем ТОКЕН в DataStore (теперь используем accessToken)
            tokenManager.saveToken(response.accessToken)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Ошибка входа: ${e.message}"))
        }
    }

    override suspend fun logout() {
        tokenManager.clearToken() // Удаляем токен
    }

    override fun getAuthToken(): Flow<String?> {
        return tokenManager.getToken()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AuthRepositoryImpl> {
        override fun createFromParcel(parcel: Parcel): AuthRepositoryImpl {
            return AuthRepositoryImpl(parcel)
        }

        override fun newArray(size: Int): Array<AuthRepositoryImpl?> {
            return arrayOfNulls(size)
        }
    }
}