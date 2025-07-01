package com.example.duskskyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserProfileDto(
    @SerializedName("user_id")
    val userId: String,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("banner_url")
    val bannerUrl: String,

    @SerializedName("bio")
    val bio: String? = null,

    @SerializedName("about_section")
    val aboutSection: String? = null,

    @SerializedName("media")
    val media: List<MediaDto> = emptyList(),

    @SerializedName("favorite_genres")
    val favoriteGenres: List<String> = emptyList(),

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)
