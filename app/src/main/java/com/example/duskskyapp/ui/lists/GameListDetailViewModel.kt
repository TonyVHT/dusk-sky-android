package com.example.duskskyapp.ui.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.remote.dto.CreateGameListItemRequest
import com.example.duskskyapp.data.remote.dto.GameListDto
import com.example.duskskyapp.data.repository.GameListRepository
import com.example.duskskyapp.data.repository.GameRepository
import com.example.duskskyapp.ui.lists.model.GameListItemUI
import com.example.duskskyapp.ui.lists.model.GameListUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameListDetailViewModel @Inject constructor(
    private val gameListRepository: GameListRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _list = MutableStateFlow<GameListUI?>(null)
    val list: StateFlow<GameListUI?> = _list

    private val _items = MutableStateFlow<List<GameListItemUI>>(emptyList())
    val items: StateFlow<List<GameListItemUI>> = _items

    fun loadListWithItems(listId: String) {
        viewModelScope.launch {
            println("🔄 Cargando lista con ID: $listId")
            try {
                val listDto = gameListRepository.getListById(listId)
                println("✅ Lista obtenida: ${listDto.name} con descripción: ${listDto.description}")

                val itemsDto = gameListRepository.getItemsByListId(listId)
                println("🔹 Total de items en la lista: ${itemsDto.size}")

                val gameItems = itemsDto.mapNotNull { item ->
                    val gameId = item.gameId ?: return@mapNotNull null
                    val listIdResolved = item.listId ?: return@mapNotNull null

                    val game = try {
                        gameRepository.getGameById(gameId)
                    } catch (e: Exception) {
                        println("❌ Error al obtener juego $gameId: ${e.message}")
                        null
                    }

                    game?.let {
                        GameListItemUI(
                            id = item.id,
                            gameId = gameId,
                            listId = listIdResolved,
                            title = it.title,
                            coverUrl = it.coverImageUrl ?: "https://via.placeholder.com/100",
                            comment = item.comment.orEmpty()
                        )
                    }
                }

                _list.value = listDto.toUI()
                _items.value = gameItems

            } catch (e: Exception) {
                println("❌ Error general al cargar lista y juegos: ${e.message}")
            }
        }
    }

    fun addGameToList(listId: String, gameId: String, comment: String = "") {
        viewModelScope.launch {
            try {
                val request = CreateGameListItemRequest(
                    listId = listId,
                    gameId = gameId,
                    comment = comment
                )

                gameListRepository.addItemToList(listId, request)
                println("✅ Juego agregado correctamente")

                loadListWithItems(listId)
            } catch (e: Exception) {
                println("❌ Error al agregar juego: ${e.message}")
            }
        }
    }

    fun removeGameFromList(itemId: String, listId: String) {
        viewModelScope.launch {
            try {
                gameListRepository.deleteItem(listId, itemId)
                println("✅ Juego eliminado correctamente")

                _items.value = _items.value.filterNot { it.id == itemId }

            } catch (e: Exception) {
                println("❌ Error al eliminar juego: ${e.message}")
            }
        }
    }

    fun deleteGameList(listId: String, onDeleted: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                gameListRepository.deleteList(listId)
                println("🗑️ Lista $listId eliminada correctamente")
                onDeleted()
            } catch (e: Exception) {
                println("❌ Error al eliminar la lista: ${e.message}")
            }
        }
    }

    private fun GameListDto.toUI(): GameListUI = GameListUI(
        id = id,
        name = name,
        description = description,
        imageUrl = "https://via.placeholder.com/100",
        gameCount = 0
    )
}
