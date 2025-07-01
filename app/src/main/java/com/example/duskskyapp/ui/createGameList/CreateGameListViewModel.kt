package com.example.duskskyapp.ui.createGameList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.local.UserPreferences
import com.example.duskskyapp.data.remote.dto.CreateGameListItemRequest
import com.example.duskskyapp.data.remote.dto.CreateGameListRequest
import com.example.duskskyapp.data.remote.dto.GameDto
import com.example.duskskyapp.data.remote.dto.GameListDto
import com.example.duskskyapp.data.repository.GameListRepository
import com.example.duskskyapp.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGameListViewModel @Inject constructor(
    private val gameListRepository: GameListRepository,
    private val gameRepository: GameRepository,
    private val userPrefs: UserPreferences
) : ViewModel() {

    val title = MutableStateFlow("")
    val description = MutableStateFlow("")
    val isPublic = MutableStateFlow(true)

    val searchQuery = MutableStateFlow("")
    val searchResults = MutableStateFlow<List<GameDto>>(emptyList())
    val selectedGames = MutableStateFlow<List<GameDto>>(emptyList())

    val isLoading = MutableStateFlow(false)
    val creationSuccess = MutableStateFlow(false)
    val errorMessage = MutableStateFlow<String?>(null)
    val createdListId = MutableStateFlow<String?>(null) // ✅ Nuevo

    fun searchGames(query: String) {
        searchQuery.value = query
        viewModelScope.launch {
            try {
                val results = gameRepository.searchGames(query)
                searchResults.value = results
            } catch (e: Exception) {
                errorMessage.value = "Error al buscar juegos"
            }
        }
    }

    fun addGameToList(game: GameDto) {
        selectedGames.update { current ->
            if (game !in current) current + game else current
        }
    }

    fun removeGameFromList(game: GameDto) {
        selectedGames.update { current -> current - game }
    }

    fun createGameList() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            creationSuccess.value = false
            createdListId.value = null

            try {
                val userId = userPrefs.getUserId()
                    ?: throw Exception("Usuario no autenticado")

                val request = CreateGameListRequest(
                    userId = userId,
                    name = title.value.trim(),
                    description = description.value.trim(),
                    isPublic = isPublic.value
                )

                val createdList: GameListDto = gameListRepository.createList(request)

                for (game in selectedGames.value) {
                    val item = CreateGameListItemRequest(
                        listId = createdList.id,
                        gameId = game.id,
                        comment = "" // opcional
                    )
                    gameListRepository.addItemToList(createdList.id, item)
                }

                createdListId.value = createdList.id // ✅ aquí se guarda el ID
                creationSuccess.value = true
            } catch (e: Exception) {
                errorMessage.value = "Error al crear la lista: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }
}
