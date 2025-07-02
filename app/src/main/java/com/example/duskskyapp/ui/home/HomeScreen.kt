package com.example.duskskyapp.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.duskskyapp.data.local.UserPreferences
import com.example.duskskyapp.data.model.GameUI
import com.example.duskskyapp.ui.friends.FriendsScreen
import com.example.duskskyapp.ui.profile.GameProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    popularGames: List<GameUI>,
    onGameClick: (String) -> Unit,
    onLogout: () -> Unit,
    onNavigateToLists: (String) -> Unit,
    onOpenDrawer: () -> Unit,
    userPrefs: UserPreferences,
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val tabs = listOf("Juegos", "Perfil", "Listas", "Amigos")
    var selectedTab by remember { mutableStateOf(0) }
    val userId by viewModel.userId.collectAsState()

    // Búsqueda de juegos
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isSearching = searchQuery.isNotBlank()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Popular") },
                    navigationIcon = {
                        IconButton(onClick = onOpenDrawer) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menú")
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
        }
        // Sin floatingActionButton
    ) { innerPadding ->
        when (selectedTab) {
            0 -> {
                // ==== Tab de Juegos con buscador funcional ====
                Column(modifier = Modifier.padding(innerPadding)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.searchGames(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        placeholder = { Text("Buscar juego...") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Search, null) }
                    )

                    PopularGrid(
                        games = if (isSearching) searchResults else popularGames,
                        onGameClick = onGameClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            1 -> {
                if (userId != null) {
                    GameProfileScreen(
                        userId      = userId!!,
                        onGameClick = onGameClick
                    )
                } else {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No se pudo cargar el perfil.")
                    }
                }
            }

            2 -> {
                if (userId != null) {
                    LaunchedEffect(Unit) {
                        onNavigateToLists(userId!!)
                    }
                } else {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No se pudo cargar tu usuario.")
                    }
                }
            }

            3 -> {
                if (userId != null) {
                    FriendsScreen(
                        onFriendClick = { friendUserId ->
                            navController.navigate("gameProfile/$friendUserId")
                        }
                    )
                } else {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No se pudo cargar el usuario.")
                    }
                }
            }

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


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PopularGrid(
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
        LazyVerticalGrid(
            columns             = GridCells.Fixed(2),
            contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement   = Arrangement.spacedBy(12.dp)
        ) {
            items(games) { game ->
                GameCard(
                    game     = game,
                    onClick  = onGameClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.7f)
                )
            }
        }
    }
}

@Composable
private fun GameCard(
    game: GameUI,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onClick(game.id) }
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
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}
