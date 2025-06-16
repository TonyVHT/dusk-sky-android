// app/src/main/java/com/example/duskskyapp/ui/detail/GameDetailScreen.kt
package com.example.duskskyapp.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.duskskyapp.data.model.GameDetailUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
    gameId: String,
    onBack: () -> Unit,
    viewModel: GameDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(gameId) { viewModel.load(gameId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = state.game?.title.orEmpty()) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        state.game?.let { game ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                AsyncImage(
                    model = game.headerImageUrl,
                    contentDescription = game.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = game.description,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Screenshots",
                    modifier = Modifier.padding(start = 16.dp),
                    style   = MaterialTheme.typography.titleSmall
                )

                LazyRow(
                    modifier             = Modifier.padding(vertical = 8.dp),
                    contentPadding       = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(game.screenshots) { url ->
                        AsyncImage(
                            model             = url,
                            contentDescription = null,
                            modifier           = Modifier
                                .size(width = 160.dp, height = 90.dp)
                                .padding(horizontal = 8.dp),
                            contentScale       = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}
