// app/src/main/java/com/example/duskskyapp/ui/home/HomeScreen.kt
package com.example.duskskyapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.duskskyapp.data.model.GameUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    popularGames: List<GameUI>,
    onGameClick: (String) -> Unit,
    onLogout: () -> Unit,
    onOpenDrawer: () -> Unit,
    onSearch: () -> Unit,
    onFabClick: () -> Unit
) {
    val tabs = listOf("Juegos", "Reseñas", "Listas", "Journal")
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Popular") },
                    navigationIcon = {
                        IconButton(onClick = onOpenDrawer) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = onSearch) {
                            Icon(Icons.Filled.Search, contentDescription = "Buscar")
                        }
                    }
                )
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { i, t ->
                        Tab(
                            selected = i == selectedTab,
                            onClick  = { selectedTab = i },
                            text     = { Text(t) }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar")
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> PopularRow(
                games    = popularGames,
                onGameClick = onGameClick,      // ← lo pasamos aquí
                modifier = Modifier.padding(innerPadding)
            )
            else -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Próximamente…")
            }
        }
    }
}

@Composable
private fun PopularRow(
    games: List<GameUI>,
    onGameClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            "Popular this week",
            style    = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(start = 16.dp, bottom = 8.dp)
        )
        LazyRow(
            contentPadding       = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(games) { game ->
                GameCard(
                    game    = game,
                    onClick = onGameClick   // ← aquí
                )
            }
        }
    }
}

@Composable
private fun GameCard(
    game: GameUI,
    onClick: (String) -> Unit      // ← recibe el callback
) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .wrapContentHeight()
            .clickable { onClick(game.id) }  // ← lo dispara
    ) {
        AsyncImage(
            model           = game.coverUrl,
            contentDescription = game.title,
            contentScale      = ContentScale.Crop,
            modifier          = Modifier
                .height(180.dp)
                .fillMaxWidth()
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text     = game.title,
            style    = MaterialTheme.typography.bodySmall,
            maxLines = 1
        )
    }
}
