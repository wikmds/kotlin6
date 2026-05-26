
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

application {
    mainClass.set("com.example.ApplicationKt")
}

kotlin {
    jvmToolchain(17)
}
dependencies {
    val ktorVersion = "2.3.12" // Оставляем твою версию Ktor

    implementation("org.jetbrains.exposed:exposed-java-time:0.55.0")
    // --- Базовые плагины Ktor ---
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-cors-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion") // Для запросов к публичному API
    implementation("io.ktor:ktor-client-cio-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation-jvm:$ktorVersion")

    // --- Exposed + PostgreSQL (Работа с БД) ---
    implementation("org.jetbrains.exposed:exposed-core:0.55.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.55.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.55.0")
    implementation("org.postgresql:postgresql:42.7.4")

    // --- HikariCP (Пул соединений) ---
    implementation("com.zaxxer:HikariCP:6.0.0")

    // --- JWT и Шифрование паролей ---
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("at.favre.lib:bcrypt:0.10.2")

    // --- Документация (Swagger / Redocly из задания) ---
    implementation("io.github.smiley4:ktor-swagger-ui:3.2.0")

    // --- Логирование ---
    implementation("ch.qos.logback:logback-classic:1.5.6")
}