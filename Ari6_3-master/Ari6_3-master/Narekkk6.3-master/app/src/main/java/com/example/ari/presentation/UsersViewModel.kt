package com.example.ari.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ari.domain.User
import com.example.ari.domain.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsersViewModel(private val repository: UserRepository) : ViewModel() {

    private val _usersState = MutableStateFlow<UiState<List<User>>>(UiState.Loading)
    val usersState: StateFlow<UiState<List<User>>> = _usersState.asStateFlow()

    private val _detailState = MutableStateFlow<UiState<User>>(UiState.Loading)
    val detailState: StateFlow<UiState<User>> = _detailState.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _usersState.value = UiState.Loading
            val result = repository.getUsers()
            result.fold(
                onSuccess = { _usersState.value = UiState.Success(it) },
                onFailure = { _usersState.value = UiState.Error(it.localizedMessage ?: "Ошибка") }
            )
        }
    }

    fun loadUserDetail(id: Int) {
        viewModelScope.launch {
            _detailState.value = UiState.Loading
            val result = repository.getUserById(id)
            result.fold(
                onSuccess = { _detailState.value = UiState.Success(it) },
                onFailure = { _detailState.value = UiState.Error(it.localizedMessage ?: "Ошибка") }
            )
        }
    }
}