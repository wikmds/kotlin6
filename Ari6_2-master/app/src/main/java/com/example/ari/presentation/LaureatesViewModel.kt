package com.example.ari.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ari.domain.GetLaureatesUseCase
import com.example.ari.domain.Laureate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LaureatesViewModel(
    private val getLaureatesUseCase: GetLaureatesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<LaureatesState>(LaureatesState.Loading)
    val state: StateFlow<LaureatesState> = _state.asStateFlow()

    // Состояния фильтров
    var currentYearFilter = MutableStateFlow("")
    var currentCategoryFilter = MutableStateFlow("All")

    init {
        loadLaureates()
    }

    fun loadLaureates() {
        viewModelScope.launch {
            _state.value = LaureatesState.Loading

            val year = currentYearFilter.value.takeIf { it.isNotBlank() }
            val category = currentCategoryFilter.value.takeIf { it != "All" }

            val result = getLaureatesUseCase(year, category)
            result.fold(
                onSuccess = { laureates ->
                    _state.value = LaureatesState.Success(laureates)
                },
                onFailure = { error ->
                    _state.value = LaureatesState.Error(error.localizedMessage ?: "Неизвестная ошибка")
                }
            )
        }
    }

    // Для экрана деталей
    fun getLaureateById(id: String): Laureate? {
        val currentState = _state.value
        if (currentState is LaureatesState.Success) {
            return currentState.laureates.find { it.id == id }
        }
        return null
    }

    fun updateYearFilter(year: String) {
        currentYearFilter.value = year
    }

    fun updateCategoryFilter(category: String) {
        currentCategoryFilter.value = category
    }
}

class LaureatesViewModelFactory(
    private val getLaureatesUseCase: GetLaureatesUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LaureatesViewModel(getLaureatesUseCase) as T
    }
}