package com.example.ari.data

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json

object NetworkModule {

    // Мы передаем TokenManager, чтобы Ktor мог сам доставать токен из памяти
    fun createClient(tokenManager: TokenManager): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO
            }

            //плагин авторизации
            install(Auth) {
                bearer {
                    loadTokens {
                        val token = tokenManager.getToken().firstOrNull()
                        if (token != null) {
                            BearerTokens(token, "") // В этом нет токена
                        } else null
                    }
                    sendWithoutRequest { true } //Отправляем токен сразу, не дожидаясь ошибки сервера
                }
            }

            // Базовый URL
            defaultRequest {
                url("https://dummyjson.com/")
                contentType(ContentType.Application.Json)
            }
        }
    }
}
