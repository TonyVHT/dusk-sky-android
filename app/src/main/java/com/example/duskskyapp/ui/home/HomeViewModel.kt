package com.example.duskskyapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.model.GameUI
import com.example.duskskyapp.data.repository.GameRepository
import com.example.duskskyapp.data.local.UserPreferences
import com.example.duskskyapp.data.remote.dto.toGameUIList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: GameRepository,
    private val userPrefs: UserPreferences
) : ViewModel() {

    // Juegos populares (carga inicial)
    private val _popularGames = MutableStateFlow<List<GameUI>>(emptyList())
    val popularGames: StateFlow<List<GameUI>> = _popularGames

    // ID del usuario autenticado
    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    // --- Búsqueda ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableStateFlow<List<GameUI>>(emptyList())
    val searchResults: StateFlow<List<GameUI>> = _searchResults

    init {
        viewModelScope.launch {
            _popularGames.value = repo.fetchPopular()
            _userId.value = userPrefs.getUserId()
        }
    }

    fun searchGames(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            try {
                _searchResults.value = repo.searchGames(query).toGameUIList()
            } catch (e: Exception) {
                _searchResults.value = emptyList()
            }
        }
    }
    // --------- AQUÍ va la función para extraer el App ID ---------
    private fun extractSteamAppId(url: String): Int? {
        val regex = Regex("""/app/(\d+)""")
        return regex.find(url)?.groups?.get(1)?.value?.toIntOrNull()
    }

    // --------- IMPORTA el juego desde la URL usando el repo ---------
    fun importGameFromSteamUrl(url: String, onResult: (Boolean, String) -> Unit) {
        val steamAppId = extractSteamAppId(url)
        if (steamAppId == null) {
            onResult(false, "URL inválida. No se pudo extraer el Steam App ID.")
            return
        }
        viewModelScope.launch {
            try {
                val result = repo.importGame(steamAppId)
                // Recargar populares (opcional)
                _popularGames.value = repo.fetchPopular()
                onResult(true, result.message)
            } catch (e: Exception) {
                val msg = e.message ?: "Error al importar juego"
                onResult(false, msg)
            }
        }
    }

}
