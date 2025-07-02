package com.example.duskskyapp.ui.friends

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.duskskyapp.data.local.UserPreferencesImpl
import com.example.duskskyapp.ui.friends.model.PendingRequestUI

private const val DEFAULT_AVATAR_URL = "http://10.0.2.2:8003/static/avatars/default_avatar.jpg"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreen(
    viewModel: FriendsViewModel = hiltViewModel(),
    friendshipViewModel: FriendshipViewModel = hiltViewModel(),
    onFriendClick: (String) -> Unit
) {
    var search by remember { mutableStateOf("") }
    val friends by viewModel.friends.collectAsState()
    val results by viewModel.searchResults.collectAsState()
    val pendingRequests by friendshipViewModel.pendingRequests.collectAsState()
    var userId by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val userPreferences = remember { UserPreferencesImpl(context) }

    LaunchedEffect(Unit) {
        val id = userPreferences.getUserId()
        userId = id
        if (id != null) {
            viewModel.loadFriends(id)
            friendshipViewModel.loadPendingRequests(id)
        }
    }

    if (userId == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
                .padding(top = 100.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Amigos",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            // === Bloque de solicitudes pendientes ===
            if (pendingRequests.isNotEmpty()) {
                Text(
                    text = "Solicitudes de amistad",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(pendingRequests) { req ->
                        PendingRequestCard(
                            senderName = req.senderName,
                            senderId = req.senderId,
                            receiverId = req.receiverId,
                            onAccept = { friendshipViewModel.acceptRequest(req.requestId.orEmpty()) },
                            onReject = { friendshipViewModel.rejectRequest(req.requestId.orEmpty()) }
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // === Buscador y amigos ===
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = search,
                    onValueChange = {
                        search = it
                        viewModel.searchUsers(it)
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Buscar usuario") },
                    singleLine = true
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = { viewModel.searchUsers(search) }) {
                    Text("Buscar")
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(Modifier.fillMaxSize()) {
                // === Lista de amigos ===
                Column(Modifier.weight(1f)) {
                    if (friends.isEmpty()) {
                        Text("Ups, no tienes amigos aún.", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            "Empieza buscando a alguien con el campo de arriba.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "Tus amigos",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(friends) { user ->
                                FriendCard(
                                    userId = user.id,
                                    name = user.name,
                                    avatarUrl = null,
                                    onClick = onFriendClick
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.width(16.dp))

                // === Resultados de búsqueda ===
                Column(Modifier.weight(1f)) {
                    if (search.isNotBlank()) {
                        Text(
                            text = "Jugadores encontrados",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        if (results.isEmpty()) {
                            Text("No se encontró ninguna persona con ese nombre.", style = MaterialTheme.typography.bodyMedium)
                        } else {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(results) { user ->
                                    val isFriend = friends.any { it.id == user.id }
                                    val alreadyRequested = pendingRequests.any {
                                        (it.senderId == userId && it.receiverId == user.id) ||
                                                (it.receiverId == userId && it.senderId == user.id)
                                    }
                                    val isSelf = user.id == userId

                                    when {
                                        isFriend || alreadyRequested || isSelf -> FriendCard(
                                            userId = user.id,
                                            name = user.name,
                                            avatarUrl = DEFAULT_AVATAR_URL,
                                            onClick = onFriendClick
                                        )
                                        else -> FriendCardWithAddButton(
                                            name = user.name,
                                            avatarUrl = DEFAULT_AVATAR_URL,
                                            onAdd = {
                                                friendshipViewModel.sendFriendRequest(userId!!, user.id)
                                            }
                                        )
                                    }
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
fun FriendCard(
    userId: String,
    name: String,
    avatarUrl: String? = null,
    onClick: (String) -> Unit
) {
    val imageUrl = avatarUrl ?: DEFAULT_AVATAR_URL
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick(userId) }, // <--- Aquí navegas
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = name,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Text(name, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun FriendCardWithAddButton(
    name: String,
    avatarUrl: String? = null,
    onAdd: () -> Unit
) {
    val imageUrl = avatarUrl ?: DEFAULT_AVATAR_URL

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = name,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium, // más pequeño
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onAdd,
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Agregar", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}


@Composable
fun PendingRequestCard(
    senderName: String,
    senderId: String,
    receiverId: String,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Solicitud de $senderName", modifier = Modifier.weight(1f))
            Button(onClick = onAccept) { Text("Aceptar") }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(onClick = onReject) { Text("Rechazar") }
        }
    }
}