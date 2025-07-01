package com.example.duskskyapp.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.duskskyapp.data.model.ReviewUI
import com.example.duskskyapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameReviewsViewModel @Inject constructor(
    private val repository: ReviewRepository
) : ViewModel() {

    private val _reviews = MutableStateFlow<List<ReviewUI>>(emptyList())
    val reviews: StateFlow<List<ReviewUI>> = _reviews.asStateFlow()

    fun loadReviews(gameId: String) {
        viewModelScope.launch {
            _reviews.value = repository.getReviewsByGame(gameId)
        }
    }
}
