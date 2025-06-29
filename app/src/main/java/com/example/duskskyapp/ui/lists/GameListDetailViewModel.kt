package com.example.duskskyapp.ui.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.remote.dto.GameListDto
import com.example.duskskyapp.data.remote.dto.GameListItemDto
import com.example.duskskyapp.data.repository.GameListRepository
import com.example.duskskyapp.ui.lists.model.GameListItemUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameListDetailViewModel @Inject constructor(
    private val repository: GameListRepository
) : ViewModel() {

    private val _list = MutableStateFlow<GameListUI?>(null)
    val list: StateFlow<GameListUI?> = _list

    private val _items = MutableStateFlow<List<GameListItemUI>>(emptyList())
    val items: StateFlow<List<GameListItemUI>> = _items

    fun loadListWithItems(listId: String) {
        viewModelScope.launch {
            try {
                val listDto = repository.getListById(listId)
                val itemsDto = repository.getItemsByListId(listId)

                _list.value = listDto.toUI()
                _items.value = itemsDto.map { it.toUI() }
            } catch (e: Exception) {
                e.printStackTrace()
                // TODO: Mostrar error en UI o con Toast
            }
        }
    }

    // 🔁 Mapper de GameListDto → GameListUI
    private fun GameListDto.toUI(): GameListUI = GameListUI(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUrl = "https://via.placeholder.com/100", // TODO: usar imagen real si backend la devuelve
        gameCount = 0 // TODO: si backend devuelve cantidad, ajusta
    )

    // 🔁 Mapper de GameListItemDto → GameListItemUI
    private fun GameListItemDto.toUI(): GameListItemUI = GameListItemUI(
        id = this.id,
        gameId = this.gameId,
        listId = this.listId,
        title = "Juego desconocido", // TODO: buscar nombre real vía GameService
        coverUrl = "https://via.placeholder.com/100", // TODO: igual para imagen
        comment = this.comment
    )
}
