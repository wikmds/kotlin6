package com.example.ari.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ari.domain.Laureate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaureateListScreen(
    state: LaureatesState,
    yearFilter: String,
    categoryFilter: String,
    onYearChanged: (String) -> Unit,
    onCategoryChanged: (String) -> Unit,
    onApplyFilters: () -> Unit,
    onLaureateClick: (String) -> Unit
) {
    val categories = listOf("All", "Physics", "Chemistry", "Literature", "Peace", "Medicine", "Economics")
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Нобелевские лауреаты") }) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {

            // --- БЛОК ФИЛЬТРОВ ---
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = yearFilter,
                    onValueChange = onYearChanged,
                    label = { Text("Год (напр. 2023)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = categoryFilter,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Категория") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    onCategoryChanged(selectionOption)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Button(
                onClick = onApplyFilters,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Применить фильтры")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- БЛОК СОСТОЯНИЙ (Loading, Error, Success) ---
            Box(modifier = Modifier.fillMaxSize()) {
                when (state) {
                    is LaureatesState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is LaureatesState.Error -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.align(Alignment.Center)) {
                            Text("Ошибка: ${state.message}", color = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = onApplyFilters) { Text("Повторить") }
                        }
                    }
                    is LaureatesState.Success -> {
                        if (state.laureates.isEmpty()) {
                            Text("Лауреаты не найдены", modifier = Modifier.align(Alignment.Center))
                        } else {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(state.laureates) { laureate ->
                                    LaureateCard(laureate, onClick = { onLaureateClick(laureate.id) })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LaureateCard(laureate: Laureate, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = laureate.fullName, style = MaterialTheme.typography.titleMedium)
            Text(text = "${laureate.awardYear} • ${laureate.category}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))

            // Обрезаем до 100 символов, как в методичке
            val shortMotivation = if (laureate.motivation.length > 100) {
                laureate.motivation.take(100) + "..."
            } else laureate.motivation

            Text(text = shortMotivation, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}