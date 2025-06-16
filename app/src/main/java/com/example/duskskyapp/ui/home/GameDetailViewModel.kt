package com.example.duskskyapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.model.GameDetailUI
import com.example.duskskyapp.data.repository.real.DefaultGameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailViewModel @Inject constructor(
    private val repo: DefaultGameRepository
) : ViewModel() {

    private val _ui = MutableStateFlow<GameDetailUI?>(null)
    val ui: StateFlow<GameDetailUI?> = _ui

    fun load(gameId: String) {
        viewModelScope.launch {
            _ui.value = repo.fetchGameDetail(gameId)
        }
    }
}
