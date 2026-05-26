package com.example.plugins

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.ktor.server.application.*

fun Application.configureSwagger() {
    install(SwaggerUI) {
        info {
            title = "Nobel Prize API"
            version = "1.0"
            description = "API для Нобелевских премий"
        }
    }
}