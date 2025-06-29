package com.example.duskskyapp.data.repository

import com.example.duskskyapp.data.remote.dto.GameListDto
import com.example.duskskyapp.data.remote.dto.GameListItemDto

interface GameListRepository {

    suspend fun getListsByUser(userId: String): List<GameListDto>

    suspend fun getListById(id: String): GameListDto

    suspend fun createList(list: GameListDto): GameListDto

    suspend fun updateList(id: String, list: GameListDto)

    suspend fun deleteList(id: String)

    suspend fun getMostRecentLists(): List<GameListDto>

    suspend fun getMostLikedLists(): List<GameListDto>

    suspend fun likeList(id: String)

    suspend fun unlikeList(id: String)

    suspend fun getItemsByListId(listId: String): List<GameListItemDto>

    suspend fun addItemToList(listId: String, item: GameListItemDto): GameListItemDto

    suspend fun updateItem(listId: String, itemId: String, item: GameListItemDto)

    suspend fun deleteItem(listId: String, itemId: String)
}
