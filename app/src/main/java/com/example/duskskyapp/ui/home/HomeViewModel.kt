package com.example.duskskyapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.model.GameUI
import com.example.duskskyapp.data.repository.GameRepository
import com.example.duskskyapp.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: GameRepository,
    private val userPrefs: UserPreferences        // ← aquí inyectamos las preferencias
) : ViewModel() {

    private val _popularGames = MutableStateFlow<List<GameUI>>(emptyList())
    val popularGames: StateFlow<List<GameUI>> = _popularGames

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    init {
        viewModelScope.launch {
            _popularGames.value = repo.fetchPopular()
            _userId.value = userPrefs.getUserId() // ← aquí cargamos el ID del usuario
        }
    }
}
