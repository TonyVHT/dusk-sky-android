package com.example.duskskyapp.ui.lists

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    onBack: () -> Unit,
    viewModel: GameListDetailViewModel = hiltViewModel()
) {
    val list by viewModel.list.collectAsState()
    val items by viewModel.items.collectAsState()

    LaunchedEffect(listId) {
        viewModel.loadListWithItems(listId)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Botón de regreso
        Button(onClick = onBack) {
            Text("Volver")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Título y descripción
        list?.let {
            Text(it.name, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(it.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Contenido
        if (items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(items) { item ->
                    GameItemRow(item = item, onGameClick = onGameClick)
                }
            }
        }
    }
}

@Composable
fun GameItemRow(item: GameListItemUI, onGameClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onGameClick(item.gameId) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = item.coverUrl.ifEmpty { "https://via.placeholder.com/100" }
                ),
                contentDescription = item.title,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(item.title, style = MaterialTheme.typography.titleMedium)
                if (item.comment.isNotEmpty()) {
                    Text("Nota: ${item.comment}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
