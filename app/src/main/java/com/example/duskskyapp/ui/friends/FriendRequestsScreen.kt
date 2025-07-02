package com.example.duskskyapp.ui.friends

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FriendRequestsScreen(
    userId: String,
    viewModel: FriendshipViewModel
) {
    val requests by viewModel.pendingRequests.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        if (requests.isEmpty()) {
            Text("No tienes solicitudes pendientes.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(requests) { req ->
                    PendingRequestCard(
                        senderName = req.senderName,
                        onAccept = { viewModel.acceptRequest(req.requestId.orEmpty()) },
                        onReject = { viewModel.rejectRequest(req.requestId.orEmpty()) }
                    )
                }
            }
        }
    }
}

@Composable
fun PendingRequestCard(
    senderName: String,
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
