package com.example.ari.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Создаем DataStore
private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class TokenManager(private val context: Context) {
    companion object {
        val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private const val TAG = "TokenManagerDebug"
    }

    // Получить токен (Flow позволяет UI реагировать на изменения токена)
    fun getToken(): Flow<String?> {
        Log.d(TAG, "getToken: Запрос потока данных начат")
        return context.dataStore.data
            .catch { exception ->
                // Обработка ошибок чтения (например, если файл поврежден)
                if (exception is IOException) {
                    Log.e(TAG, "getToken: Ошибка при чтении DataStore", exception)
                } else {
                    throw exception
                }
            }
            .map { prefs ->
                val token = prefs[TOKEN_KEY]
                Log.d(TAG, "getToken: Данные получены. Токен пустой? ${token == null}")
                token
            }
    }

    // Сохранить токен после успешного логина
    suspend fun saveToken(token: String) {
        Log.d(TAG, "saveToken: Попытка сохранения токена...")
        try {
            context.dataStore.edit { prefs ->
                prefs[TOKEN_KEY] = token
            }
            Log.d(TAG, "saveToken: Токен успешно сохранен")
        } catch (e: Exception) {
            Log.e(TAG, "saveToken: Ошибка при сохранении токена", e)
        }
    }

    // Удалить токен при выходе
    suspend fun clearToken() {
        Log.d(TAG, "clearToken: Попытка удаления токена...")
        try {
            context.dataStore.edit { prefs ->
                prefs.remove(TOKEN_KEY)
            }
            Log.d(TAG, "clearToken: Токен успешно удален")
        } catch (e: Exception) {
            Log.e(TAG, "clearToken: Ошибка при удалении токена", e)
        }
    }
}
