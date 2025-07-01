package com.example.duskskyapp.ui.lists

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.duskskyapp.ui.lists.model.GameListUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameListsScreen(
    userId: String,
    onGameClick: (String) -> Unit,
    onListClick: (String) -> Unit,
    onBack: () -> Unit,
    onCreateListClick: () -> Unit, // ✅ nuevo parámetro
    viewModel: GameListsViewModel = hiltViewModel()
) {
    val lists by viewModel.lists.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadUserLists(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // 🟦 Banner principal
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Colecciona, organiza y comparte.",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Las listas son la forma perfecta de agrupar tus juegos favoritos.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = onCreateListClick, // ✅ aquí llamamos al callback
                        shape = RoundedCornerShape(50),
                    ) {
                        Text("Empieza tu propia lista")
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Listas Populares Recientes",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            if (lists.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(8.dp))
                    Text("Cargando listas…")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(lists) { list: GameListUI ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onListClick(list.id) },
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(modifier = Modifier.padding(12.dp)) {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        model = list.imageUrl?.takeIf { it.isNotEmpty() } ?: "https://via.placeholder.com/100"
                                    ),
                                    contentDescription = list.name.ifBlank { "Imagen de la lista" },
                                    modifier = Modifier.size(80.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(
                                        list.name.ifBlank { "Lista sin nombre" },
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        list.description?.ifBlank { "Sin descripción" } ?: "Sin descripción",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text("${list.gameCount} juegos", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
