package com.example.ari.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ari.domain.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val loginState: StateFlow<UiState<Unit>> = _loginState.asStateFlow()

    fun login(username: String, pass: String) {
        viewModelScope.launch {
            _loginState.value = UiState.Loading
            val result = repository.login(username, pass)
            result.fold(
                onSuccess = { _loginState.value = UiState.Success(Unit) },
                onFailure = { _loginState.value = UiState.Error(it.localizedMessage ?: "Ошибка") }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _loginState.value = UiState.Idle // Сброс состояния логина
        }
    }
}