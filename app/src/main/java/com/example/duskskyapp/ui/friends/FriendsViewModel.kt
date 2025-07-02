package com.example.duskskyapp.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.remote.AuthApi
import com.example.duskskyapp.data.remote.UserManagerApi
import com.example.duskskyapp.data.remote.dto.UserDto
import com.example.duskskyapp.data.repository.FriendshipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val userManagerApi: UserManagerApi,
    private val friendshipRepository: FriendshipRepository,
    private val authApi: AuthApi
) : ViewModel() {

    private val _friends = MutableStateFlow<List<UserDto>>(emptyList())
    val friends: StateFlow<List<UserDto>> = _friends

    private val _searchResults = MutableStateFlow<List<UserDto>>(emptyList())
    val searchResults: StateFlow<List<UserDto>> = _searchResults

    fun loadFriends(userId: String) {
        viewModelScope.launch {
            try {
                val friendshipList = friendshipRepository.getFriends(userId)
                val friendIds = friendshipList.map {
                    if (it.senderId == userId) it.receiverId else it.senderId
                }

                val friendDtos = kotlinx.coroutines.coroutineScope {
                    friendIds.map { id ->
                        async {
                            try {
                                authApi.getUserById(id)
                            } catch (e: Exception) {
                                println("⚠️ Error cargando amigo $id: ${e.message}")
                                null
                            }
                        }
                    }.awaitAll().filterNotNull()
                }

                _friends.value = friendDtos
                println("✅ Amigos cargados: ${friendDtos.map { it.name }}")

            } catch (e: Exception) {
                println("❌ Error al cargar amigos: ${e.message}")
                _friends.value = emptyList()
            }
        }
    }


    fun searchUsers(query: String) = viewModelScope.launch {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return@launch
        }

        try {
            val results = authApi.searchUserByUsername(query)
            println("🔍 Resultados de búsqueda: ${results.map { it.name }}")
            _searchResults.value = results
        } catch (e: Exception) {
            println("❌ Error al buscar usuarios: ${e.message}")
            _searchResults.value = emptyList()
        }
    }
}
