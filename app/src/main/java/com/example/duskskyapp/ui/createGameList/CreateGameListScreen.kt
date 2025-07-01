package com.example.duskskyapp.ui.createGameList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGameListScreen(
    onListCreated: (String) -> Unit,
    viewModel: CreateGameListViewModel = hiltViewModel()
) {
    val title by viewModel.title.collectAsState()
    val description by viewModel.description.collectAsState()
    val isPublic by viewModel.isPublic.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val selectedGames by viewModel.selectedGames.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val creationSuccess by viewModel.creationSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val createdListId by viewModel.createdListId.collectAsState()

    LaunchedEffect(createdListId) {
        createdListId?.let { onListCreated(it) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Nueva Lista",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            OutlinedTextField(
                value = title,
                onValueChange = { viewModel.title.value = it },
                label = { Text("Nombre de la Lista *") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = description,
                onValueChange = { viewModel.description.value = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                singleLine = false,
                maxLines = 5
            )
        }

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(checked = isPublic, onCheckedChange = { viewModel.isPublic.value = it })
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (isPublic) "Pública – Cualquiera puede verla"
                    else "Privada – Solo tú puedes verla"
                )
            }
        }

        item {
            Divider()
        }

        item {
            Text(
                text = "Añadir Juegos a la Lista",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Row {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchQuery.value = it },
                    placeholder = { Text("Busca juegos por nombre...") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = { viewModel.searchGames(searchQuery) }) {
                    Icon(Icons.Default.Search, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Buscar")
                }
            }
        }

        if (selectedGames.isNotEmpty()) {
            item {
                Text("Juegos añadidos:", style = MaterialTheme.typography.titleSmall)
            }
            items(selectedGames) { game ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(game.title)
                    TextButton(onClick = { viewModel.removeGameFromList(game) }) {
                        Text("Quitar")
                    }
                }
            }
        }

        if (searchResults.isNotEmpty()) {
            item {
                Text("Resultados:", style = MaterialTheme.typography.titleSmall)
            }
            items(searchResults) { game ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = game.coverImageUrl ?: "",
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(end = 12.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(game.title, fontWeight = FontWeight.SemiBold)
                            Text(game.description ?: "", style = MaterialTheme.typography.bodySmall)
                        }
                        val alreadyAdded = selectedGames.any { it.id == game.id }
                        TextButton(
                            onClick = { viewModel.addGameToList(game) },
                            enabled = !alreadyAdded
                        ) {
                            Text(if (alreadyAdded) "Agregado" else "Añadir")
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant) // fondo suave
                    .padding(vertical = 16.dp), // espacio arriba y abajo
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { viewModel.createGameList() },
                    enabled = title.isNotBlank() && selectedGames.isNotEmpty() && !isLoading
                ) {
                    Text("Crear Lista")
                }
            }
        }



        if (errorMessage != null) {
            item {
                Text(
                    text = errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        if (creationSuccess) {
            item {
                Text(
                    text = "¡Lista creada exitosamente!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
