package com.example.duskskyapp.data.repository

import com.example.duskskyapp.data.model.GameTrackingUI

interface UserTrackingRepository {
    suspend fun getUserTrackings(userId: String): List<GameTrackingUI>
}
