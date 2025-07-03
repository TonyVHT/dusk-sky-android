package com.example.duskskyapp.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.local.UserPreferences
import com.example.duskskyapp.data.model.CommentUI
import com.example.duskskyapp.data.model.GameTrackingUI
import com.example.duskskyapp.data.remote.UserManagerApi
import com.example.duskskyapp.data.remote.dto.UserProfileDto
import com.example.duskskyapp.data.repository.CommentRepository
import com.example.duskskyapp.data.repository.UserRepository
import com.example.duskskyapp.data.repository.UserTrackingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameProfileViewModel @Inject constructor(
    private val trackingRepo: UserTrackingRepository,
    private val userManagerApi: UserManagerApi,
    private val userRepo: UserRepository,
    private val commentRepo: CommentRepository,
    private val userPrefs: UserPreferences     // INYECTA UserPreferences
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(0)
    private val _selectedStatus = MutableStateFlow("played")
    private val _games = MutableStateFlow<List<GameTrackingUI>>(emptyList())
    private val _profile = MutableStateFlow<UserProfileDto?>(null)
    private val _username = MutableStateFlow<String>("")
    private val _authorId = MutableStateFlow<String?>(null)      // <---

    private val _comments = MutableStateFlow<List<CommentUI>>(emptyList())
    val comments: StateFlow<List<CommentUI>> = _comments

    val selectedStatus: StateFlow<String> = _selectedStatus
    val games: StateFlow<List<GameTrackingUI>> = _games
    val profile: StateFlow<UserProfileDto?> = _profile
    val username: StateFlow<String> = _username
    val authorId: StateFlow<String?> = _authorId      // <--- expón el authorId

    private var allTrackings: List<GameTrackingUI> = emptyList()

    init {
        // Al crear el ViewModel, intenta obtener el userId (authorId)
        viewModelScope.launch {
            _authorId.value = userPrefs.getUserId()
        }
    }
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
                avatarUrl = profile.avatarUrl.replace("localhost", "192.168.1.113"),
                bannerUrl = profile.bannerUrl.replace("localhost", "192.168.1.113")
            )
            _profile.value = fixedProfile

            val name = try {
                userRepo.getUserNameById(userId)
            } catch (e: Exception) {
                Log.w("GameProfile", "No se pudo obtener el nombre del usuario", e)
                "Usuario desconocido"
            }
            _username.value = name

        } catch (e: Exception) {
            Log.e("GameProfile", "Error al cargar perfil o nombre", e)
        }
    }

    fun loadProfileComments(userId: String) = viewModelScope.launch {
        try {
            val result = commentRepo.fetchComments(reviewId = userId)
            _comments.value = result
        } catch (e: Exception) {
            Log.e("GameProfile", "Error al cargar comentarios de perfil", e)
            _comments.value = emptyList()
        }
    }

    fun postProfileComment(userId: String, text: String) = viewModelScope.launch {
        val author = _authorId.value
        if (author.isNullOrBlank()) {
            Log.e("GameProfile", "No hay authorId, no se puede publicar comentario.")
            return@launch
        }
        try {
            commentRepo.postComment(
                reviewId = userId,
                authorId = author,
                text = text
            )
            loadProfileComments(userId)
        } catch (e: Exception) {
            Log.e("GameProfile", "Error al publicar comentario", e)
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