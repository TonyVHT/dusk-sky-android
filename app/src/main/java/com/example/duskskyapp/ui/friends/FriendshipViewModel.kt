package com.example.duskskyapp.ui.friends

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.remote.AuthApi
import com.example.duskskyapp.data.remote.dto.FriendshipRequestDto
import com.example.duskskyapp.data.repository.FriendshipRepository
import com.example.duskskyapp.ui.friends.model.PendingRequestUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendshipViewModel @Inject constructor(
    private val repository: FriendshipRepository,
    private val authApi: AuthApi
) : ViewModel() {

    private val _pendingRequests = MutableStateFlow<List<PendingRequestUI>>(emptyList())
    val pendingRequests: StateFlow<List<PendingRequestUI>> = _pendingRequests

    fun loadPendingRequests(userId: String) {
        viewModelScope.launch {
            try {
                val requests = repository.getPendingRequests(userId)
                val uiRequests = requests.map { req ->
                    async {
                        val sender = try {
                            authApi.getUserById(req.senderId)
                        } catch (e: Exception) {
                            null
                        }
                        PendingRequestUI(
                            requestId = req.id,
                            senderId = req.senderId,
                            receiverId = req.receiverId,
                            senderName = sender?.name ?: "Usuario desconocido"
                        )
                    }
                }.awaitAll()
                _pendingRequests.value = uiRequests
            } catch (e: Exception) {
                _pendingRequests.value = emptyList()
            }
        }
    }

    fun acceptRequest(requestId: String) {
        viewModelScope.launch {
            Log.d("FriendshipViewModel", "🔄 Intentando aceptar solicitud con ID: $requestId")
            try {
                repository.acceptRequest(requestId)
                _pendingRequests.value = _pendingRequests.value.filterNot { it.requestId == requestId }
                Log.d("FriendshipViewModel", "✅ Solicitud aceptada exitosamente para ID: $requestId")
            } catch (e: Exception) {
                Log.e("FriendshipViewModel", "❌ Error al aceptar solicitud (ID: $requestId): ${e.message}", e)
                println("❌ Error al aceptar: ${e.message}")
            }
        }
    }


    fun rejectRequest(requestId: String) {
        viewModelScope.launch {
            try {
                repository.rejectRequest(requestId)
                _pendingRequests.value = _pendingRequests.value.filterNot { it.requestId == requestId }
            } catch (e: Exception) {
                println("❌ Error al rechazar: ${e.message}")
            }
        }
    }

    fun sendFriendRequest(senderId: String, receiverId: String) {
        viewModelScope.launch {
            Log.d("FriendshipViewModel", "Enviando solicitud de amistad de $senderId a $receiverId")
            try {
                repository.sendRequest(
                    FriendshipRequestDto(
                        sender_id = senderId,
                        receiver_id = receiverId
                    )
                )
                Log.d("FriendshipViewModel", "Solicitud enviada con éxito")
            } catch (e: Exception) {
                Log.e("FriendshipViewModel", "Error al enviar solicitud: ${e.message}", e)
            }
        }
    }

}
