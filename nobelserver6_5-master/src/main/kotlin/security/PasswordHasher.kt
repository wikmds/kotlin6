package com.example.security

import at.favre.lib.crypto.bcrypt.BCrypt

object PasswordHasher {
    // Превращаем обычный пароль в хэш
    fun hash(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    // Проверяем, совпадает ли пароль с хэшем из БД
    fun verify(password: String, hash: String): Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), hash).verified
    }
}