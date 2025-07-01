package com.example.duskskyapp.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.duskskyapp.data.model.GameTrackingUI
import com.example.duskskyapp.data.remote.dto.UserProfileDto

@Composable
fun GameProfileScreen(
    userId: String,
    viewModel: GameProfileViewModel = hiltViewModel(),
    onGameClick: (String) -> Unit
) {
    val tabs = listOf("Juegos", "Favoritos", "Reseñas")
    var selectedTab by remember { mutableStateOf(0) }

    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val games by viewModel.games.collectAsState()
    val username by viewModel.username.collectAsState()
    val profile by viewModel.profile.collectAsState()

    val statusLabels = mapOf(
        "played" to "Jugados",
        "playing" to "Jugando",
        "backlog" to "Backlog",
        "abandoned" to "Abandonado"
    )

    LaunchedEffect(userId) {
        viewModel.loadUserGames(userId)
        viewModel.loadUserProfile(userId)
    }

    Column(Modifier.fillMaxSize()) {
        GameProfileHeader(username = username, profile = profile)

        Column(Modifier.padding(16.dp)) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            viewModel.selectTab(index)
                        },
                        text = { Text(title) }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            if (selectedTab == 0) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    statusLabels.forEach { (key, label) ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedStatus == key,
                                onClick = { viewModel.setStatusFilter(key) }
                            )
                            Text(label)
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 120.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(games) { game ->
                    GameCard(game = game, onClick = { onGameClick(game.gameId) })
                }
            }
        }
    }
}

@Composable
fun GameProfileHeader(username: String, profile: UserProfileDto?) {
    Box(modifier = Modifier.fillMaxWidth()) {
        profile?.bannerUrl?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 130.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            profile?.avatarUrl?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(3.dp, MaterialTheme.colorScheme.background, CircleShape)
                        .shadow(6.dp, CircleShape)
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 12.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = username,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        profile?.bio?.let {
            Text(it, fontSize = 14.sp, maxLines = 2)
        }

        profile?.aboutSection?.let {
            Text(
                it,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    Spacer(Modifier.height(12.dp))
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
