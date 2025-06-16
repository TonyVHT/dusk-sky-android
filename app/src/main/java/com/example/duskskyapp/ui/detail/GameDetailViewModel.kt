// app/src/main/java/com/example/duskskyapp/ui/detail/GameDetailViewModel.kt
package com.example.duskskyapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.model.GameDetailUI
import com.example.duskskyapp.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GameDetailUiState(
    val game: GameDetailUI? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class GameDetailViewModel @Inject constructor(
    private val repo: GameRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(GameDetailUiState())
    val uiState: StateFlow<GameDetailUiState> = _uiState

    fun load(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val detail = repo.fetchGameDetail(id)
                _uiState.value = _uiState.value.copy(game = detail, isLoading = false)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message, isLoading = false)
            }
        }
    }
}
