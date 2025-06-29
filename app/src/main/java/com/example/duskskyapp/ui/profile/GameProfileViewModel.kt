package com.example.duskskyapp.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.model.GameTrackingUI
import com.example.duskskyapp.data.remote.UserManagerApi
import com.example.duskskyapp.data.remote.dto.UserDto
import com.example.duskskyapp.data.remote.dto.UserProfileDto
import com.example.duskskyapp.data.repository.UserTrackingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameProfileViewModel @Inject constructor(
    private val trackingRepo: UserTrackingRepository,
    private val userManagerApi: UserManagerApi,
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(0)
    private val _selectedStatus = MutableStateFlow("played")
    private val _games = MutableStateFlow<List<GameTrackingUI>>(emptyList())
    private val _profile = MutableStateFlow<UserProfileDto?>(null)
    private val _user = MutableStateFlow<UserDto?>(null)

    val selectedStatus: StateFlow<String> = _selectedStatus
    val games: StateFlow<List<GameTrackingUI>> = _games
    val profile: StateFlow<UserProfileDto?> = _profile
    val user: StateFlow<UserDto?> = _user

    private var allTrackings: List<GameTrackingUI> = emptyList()

    fun loadUserGames(userId: String) = viewModelScope.launch {
        val data = trackingRepo.getUserTrackings(userId)
        Log.d("GameProfileViewModel", "Se cargaron ${data.size} trackings")
        allTrackings = data
        applyFilter()
    }

    fun loadUserProfile(userId: String) = viewModelScope.launch {
        try {
            val profile = userManagerApi.getUserProfile(userId)
            val fixedProfile = profile.copy(
                avatarUrl = profile.avatarUrl.replace("localhost", "192.168.1.68"),
                bannerUrl = profile.bannerUrl.replace("localhost", "192.168.1.68")
            )
            _profile.value = fixedProfile

            // Como no puedes buscar por ID, usamos el mismo ID que tiene el perfil como username
            val userList = userManagerApi.searchUserByUsername(userId)
            if (userList.isNotEmpty()) {
                _user.value = userList.first()
            } else {
                Log.w("GameProfile", "Usuario no encontrado con username = $userId")
            }

        } catch (e: Exception) {
            Log.e("GameProfile", "Error al cargar perfil o usuario", e)
        }
    }


    fun selectTab(index: Int) {
        _selectedTab.value = index
        applyFilter()
    }

    fun setStatusFilter(status: String) {
        _selectedStatus.value = status
        applyFilter()
    }

    private fun applyFilter() {
        val currentTab = _selectedTab.value
        val status = _selectedStatus.value

        val filtered = when (currentTab) {
            0 -> allTrackings.filter { it.status == status }
            1 -> allTrackings.filter { it.liked }
            2 -> allTrackings.filter { it.hasReview }
            else -> emptyList()
        }

        _games.value = filtered
    }
}
