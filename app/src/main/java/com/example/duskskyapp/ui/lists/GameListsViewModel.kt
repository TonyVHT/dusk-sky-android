package com.example.duskskyapp.ui.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.remote.dto.GameListDto
import com.example.duskskyapp.data.repository.GameListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameListsViewModel @Inject constructor(
    private val repository: GameListRepository
) : ViewModel() {

    private val _lists = MutableStateFlow<List<GameListUI>>(emptyList())
    val lists: StateFlow<List<GameListUI>> = _lists

    fun loadUserLists(userId: String) = viewModelScope.launch {
        try {
            val response = repository.getListsByUser(userId)
            _lists.value = response.map { it.toUI() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

// UI model para desacoplar de DTO y simplificar la vista
data class GameListUI(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val gameCount: Int = 0
)

// Transformador
fun GameListDto.toUI(): GameListUI {
    return GameListUI(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUrl = "https://via.placeholder.com/100", // TODO: si backend soporta imagen, reemplazar
        gameCount = 0 // TODO: si backend devuelve items o cantidad, ajustar
    )
}
