package com.example.data.repository

import com.example.data.database.UserTable
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.security.PasswordHasher
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserRepositoryImpl : UserRepository {

    override suspend fun registerUser(username: String, passwordHash: String): User? = newSuspendedTransaction {
        // Заменили select на selectAll().where
        if (!UserTable.selectAll().where { UserTable.username eq username }.empty()) return@newSuspendedTransaction null

        val insertStatement = UserTable.insert {
            it[UserTable.username] = username
            it[UserTable.passwordHash] = passwordHash
        }
        insertStatement.resultedValues?.singleOrNull()?.let {
            User(it[UserTable.id], it[UserTable.username], it[UserTable.role])
        }
    }

    override suspend fun findByUsername(username: String): User? = newSuspendedTransaction {
        UserTable.selectAll().where { UserTable.username eq username }.singleOrNull()?.let {
            User(it[UserTable.id], it[UserTable.username], it[UserTable.role])
        }
    }

    override suspend fun checkPassword(username: String, password: String): Boolean = newSuspendedTransaction {
        val userRow = UserTable.selectAll().where { UserTable.username eq username }.singleOrNull()
        if (userRow != null) {
            PasswordHasher.verify(password, userRow[UserTable.passwordHash])
        } else false
    }
}