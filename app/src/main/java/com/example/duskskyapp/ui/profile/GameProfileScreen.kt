package com.example.duskskyapp.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.duskskyapp.data.model.GameTrackingUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameProfileScreen(
    userId: String,
    viewModel: GameProfileViewModel = hiltViewModel(),
    onGameClick: (String) -> Unit
) {
    val games by viewModel.games.collectAsState()
    val username by viewModel.username.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val comments by viewModel.comments.collectAsState()
    val authorId by viewModel.authorId.collectAsState()

    var commentText by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        viewModel.loadUserGames(userId)
        viewModel.loadUserProfile(userId)
        viewModel.loadProfileComments(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(username) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Banner con avatar y nombre
            profile?.let {
                Box(modifier = Modifier.fillMaxWidth()) {
                    it.bannerUrl?.let { banner ->
                        Image(
                            painter = rememberAsyncImagePainter(banner),
                            contentDescription = "Banner",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(start = 16.dp, bottom = 0.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Box(modifier = Modifier.size(100.dp)) {
                            profile?.avatarUrl?.let { avatar ->
                                Image(
                                    painter = rememberAsyncImagePainter(avatar),
                                    contentDescription = "Avatar",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .border(3.dp, MaterialTheme.colorScheme.background, CircleShape)
                                        .shadow(6.dp, CircleShape)
                                )
                            }

                            // Botón de editar avatar
                            IconButton(
                                onClick = {
                                    // TODO: Lógica para subir imagen nueva
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(28.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                                        shape = CircleShape
                                    )
                                    .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar avatar",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = username,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // BIO
            profile?.bio?.let {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Biografía",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ACERCA DE
            profile?.aboutSection?.let {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Acerca de",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 120.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .heightIn(max = 260.dp)
                    .padding(horizontal = 16.dp)
            ) {
                items(games) { game ->
                    GameCard(game = game, onClick = { onGameClick(game.gameId) })
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )

            // Sección de Comentarios
            Text(
                "Comentarios:",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp, top = 12.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f, fill = true)
            ) {
                items(comments) { comment ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = comment.author,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = comment.text,
                                style = MaterialTheme.typography.bodySmall
                            )
                            if (comment.timestamp.isNotBlank()) {
                                Text(
                                    text = comment.timestamp,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
            }

            // Campo para escribir comentario
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe un comentario...") },
                    singleLine = true
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (commentText.isNotBlank() && !authorId.isNullOrBlank()) {
                            viewModel.postProfileComment(userId, commentText)
                            commentText = ""
                        }
                    },
                    enabled = commentText.isNotBlank() && !authorId.isNullOrBlank()
                ) {
                    Text("Publicar")
                }
            }
        }
    }
}

@Composable
fun GameCard(game: GameTrackingUI, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(game.gameImage),
                contentDescription = game.gameTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
            )
            Column(Modifier.padding(8.dp)) {
                Text(
                    text = game.gameTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1
                )
                Text(
                    text = "⭐ %.1f".format(game.rating),
                    fontSize = 12.sp
                )
            }
        }
    }
}