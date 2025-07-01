@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.duskskyapp.ui.lists

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.duskskyapp.ui.lists.model.GameListItemUI

@Composable
fun GameListDetailScreen(
    listId: String,
    onGameClick: (String) -> Unit,
    onBack: () -> Unit = {},
    onListDeleted: () -> Unit ,
    viewModel: GameListDetailViewModel = hiltViewModel()
) {
    val list by viewModel.list.collectAsState()
    val items by viewModel.items.collectAsState()

    LaunchedEffect(listId) {
        viewModel.loadListWithItems(listId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Lista") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.deleteGameList(listId)
                        onListDeleted()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar lista")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            list?.let {
                Text(
                    it.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    it.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            when {
                items.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay juegos en esta lista.")
                    }
                }

                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(items) { item ->
                            GameItemRow(
                                item = item,
                                onGameClick = onGameClick,
                                onRemoveClick = {
                                    viewModel.removeGameFromList(item.id, listId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameItemRow(
    item: GameListItemUI,
    onGameClick: (String) -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = item.coverUrl.ifEmpty { "https://via.placeholder.com/100" }
                ),
                contentDescription = item.title,
                modifier = Modifier
                    .size(80.dp)
                    .clickable { onGameClick(item.gameId) },
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onGameClick(item.gameId) }
            ) {
                Text(item.title, style = MaterialTheme.typography.titleMedium)
                if (item.comment.isNotEmpty()) {
                    Text("Nota: ${item.comment}", style = MaterialTheme.typography.bodySmall)
                }
            }

            IconButton(onClick = onRemoveClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar juego"
                )
            }
        }
    }
}
