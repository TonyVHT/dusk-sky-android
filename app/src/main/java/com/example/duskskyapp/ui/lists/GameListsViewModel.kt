package com.example.duskskyapp.ui.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.repository.GameListRepository
import com.example.duskskyapp.data.repository.GameRepository
import com.example.duskskyapp.ui.lists.model.GameListUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameListsViewModel @Inject constructor(
    private val listRepository: GameListRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _lists = MutableStateFlow<List<GameListUI>>(emptyList())
    val lists: StateFlow<List<GameListUI>> = _lists

    fun loadUserLists(userId: String) = viewModelScope.launch {
        try {
            val response = listRepository.getListsByUser(userId)

            val uiLists = response.map { listDto ->
                val items = try {
                    listRepository.getItemsByListId(listDto.id)
                } catch (e: Exception) {
                    emptyList()
                }

                val firstGameCover = items.firstOrNull()?.gameId?.let { gameId ->
                    try {
                        gameRepository.getGameById(gameId).coverImageUrl
                    } catch (e: Exception) {
                        null
                    }
                }

                GameListUI(
                    id = listDto.id,
                    name = listDto.name,
                    description = listDto.description,
                    imageUrl = firstGameCover ?: "https://via.placeholder.com/100",
                    gameCount = items.size
                )
            }

            _lists.value = uiLists
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
