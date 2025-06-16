// app/src/main/java/com/example/duskskyapp/ui/home/HomeViewModel.kt
package com.example.duskskyapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.model.GameUI
import com.example.duskskyapp.data.repository.GameRepository     // ← la interfaz, no real.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: GameRepository           // ← aquí inyectas la interfaz
) : ViewModel() {

    private val _popularGames = MutableStateFlow<List<GameUI>>(emptyList())
    val popularGames: StateFlow<List<GameUI>> = _popularGames

    init {
        viewModelScope.launch {
            _popularGames.value = repo.fetchPopular()
        }
    }
}
