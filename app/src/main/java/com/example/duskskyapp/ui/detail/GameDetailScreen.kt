package com.example.duskskyapp.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
    gameId: String,
    onBack: () -> Unit,
    viewModel: GameDetailViewModel = hiltViewModel()
) {
    val gameState by viewModel.gameDetail.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    var reviewText by remember { mutableStateOf("") }
    var rating by remember { mutableFloatStateOf(3.0f) }

    LaunchedEffect(gameId) {
        viewModel.loadGame(gameId)
        viewModel.loadReviews(gameId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(gameState?.title.orEmpty()) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        gameState?.let { game ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                // Imagen principal
                AsyncImage(
                    model = game.headerUrl.ifNullOrBlank {
                        "https://via.placeholder.com/600x200?text=Sin+imagen"
                    },
                    contentDescription = game.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Título y desarrollador
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = game.title ?: "Sin título",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    game.developer?.let {
                        Text(
                            text = "Desarrollado por $it",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Descripción
                Text(
                    text = game.description.orEmpty(),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Screenshots
                if (!game.screenshots.isNullOrEmpty()) {
                    Text(
                        text = "Screenshots",
                        modifier = Modifier.padding(start = 16.dp, top = 12.dp),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        items(game.screenshots) { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(width = 160.dp, height = 90.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp))

                // Reviews
                Text(
                    text = "Reseñas",
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )

                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                ) {
                    items(reviews) { review ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = review.authorName?: "Autor desconocido",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "★".repeat(review.rating.toInt()) + " (${review.rating}/5)",
                                    style = MaterialTheme.typography.labelSmall
                                )
                                Text(
                                    text = review.content,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                // Campo para escribir una review
                OutlinedTextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    placeholder = { Text("Escribe una reseña...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                // Rating selector
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text("Calificación:")
                    Slider(
                        value = rating,
                        onValueChange = { rating = it },
                        valueRange = 0f..5f,
                        steps = 4,
                        modifier = Modifier.weight(1f).padding(horizontal = 12.dp)
                    )
                    Text(String.format("%.1f", rating))
                }

                Button(
                    onClick = {
                        viewModel.postReview(gameId, reviewText, rating)
                        reviewText = ""
                        rating = 3.0f
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Publicar")
                }
            }
        }
    }
}

// Helper extension
fun String?.ifNullOrBlank(default: () -> String): String {
    return if (this.isNullOrBlank()) default() else this
}
