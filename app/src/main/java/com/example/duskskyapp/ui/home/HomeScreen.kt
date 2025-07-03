package com.example.duskskyapp.ui.home

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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

    var username by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        username = userPrefs.getUsername()
        Log.d("HomeScreen", "El usuario logueado es: $username")
    }
    val isAdmin = username == "admin"


    var showAddDialog by remember { mutableStateOf(false) }
    var isImportError by remember { mutableStateOf(false) }
    var importErrorMessage by remember { mutableStateOf("") }

    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isSearching = searchQuery.isNotBlank()

    val context = LocalContext.current // 👈 obtener contexto aquí

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("DuskSky") }
                )
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { i, t ->
                        Tab(
                            selected = i == selectedTab,
                            onClick = { selectedTab = i },
                            text = { Text(t) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> {
                Column(modifier = Modifier.padding(innerPadding)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.searchGames(it) },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            placeholder = { Text("Buscar juego...") },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Search, null) }
                        )
                        if (isAdmin) { // 👈 SOLO el admin ve este botón
                            IconButton(
                                onClick = {
                                    showAddDialog = true
                                    isImportError = false
                                    importErrorMessage = ""
                                },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Agregar juego",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }


                    PopularGrid(
                        games = if (isSearching) searchResults else popularGames,
                        onGameClick = onGameClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            1 -> {
                if (userId != null) {
                    GameProfileScreen(userId = userId!!, onGameClick = onGameClick)
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
                    FriendsScreen(onFriendClick = { navController.navigate("gameProfile/$it") })
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
        }

        if (showAddDialog) {
            AddGameDialog(
                context = context, // 👈 se lo pasamos
                onDismiss = { showAddDialog = false },
                onImportClick = { url ->
                    if (url.contains("123456")) {
                        isImportError = true
                        importErrorMessage = "El juego ya fue importado previamente."
                    } else {
                        Toast.makeText(context, "Juego importado exitosamente", Toast.LENGTH_SHORT).show()
                        isImportError = false
                        showAddDialog = false
                        // TODO: Llamar ViewModel o lógica real aquí
                    }
                },
                isError = isImportError,
                errorMessage = importErrorMessage
            )
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
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(games) { game ->
                GameCard(
                    game = game,
                    onClick = onGameClick,
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
    Column(modifier = modifier.clickable { onClick(game.id) }) {
        AsyncImage(
            model = game.coverUrl,
            contentDescription = game.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = game.title,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun AddGameDialog(
    context: Context,
    onDismiss: () -> Unit,
    onImportClick: (String) -> Unit,
    isError: Boolean,
    errorMessage: String
) {
    var url by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Agregar nuevo juego",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column {
                Text("Ingresa una URL válida de la tienda de Steam para el juego.")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    placeholder = { Text("https://store.steampowered.com/app/...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (isError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Error: $errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onImportClick(url) },
                enabled = url.isNotBlank()
            ) {
                Text("Importar juego")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}