package com.example.duskskyapp.data.repository.real

import com.example.duskskyapp.data.remote.GameListApi
import com.example.duskskyapp.data.remote.dto.*
import com.example.duskskyapp.data.repository.GameListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RealGameListRepository @Inject constructor(
    private val api: GameListApi
) : GameListRepository {

    override suspend fun createList(request: CreateGameListRequest): GameListDto {
        return api.createList(request)
    }

    override suspend fun addItemToList(listId: String, request: CreateGameListItemRequest): GameListItemDto {
        return api.addItemToList(listId, request)
    }

    override suspend fun getListsByUser(userId: String): List<GameListDto> {
        return api.getListsByUser(userId)
    }

    override suspend fun getListById(id: String): GameListDto {
        return api.getListById(id)
    }

    override suspend fun updateList(id: String, list: GameListDto) {
        api.updateList(id, list)
    }

    override suspend fun deleteList(id: String) {
        api.deleteList(id)
    }

    override suspend fun getMostRecentLists(): List<GameListDto> {
        return api.getMostRecentLists()
    }

    override suspend fun getMostLikedLists(): List<GameListDto> {
        return api.getMostLikedLists()
    }

    override suspend fun likeList(id: String) {
        api.likeList(id)
    }

    override suspend fun unlikeList(id: String) {
        api.unlikeList(id)
    }

    override suspend fun getItemsByListId(listId: String): List<GameListItemDto> {
        return api.getItemsByListId(listId)
    }

    override suspend fun updateItem(listId: String, itemId: String, item: GameListItemDto) = withContext(Dispatchers.IO) {
        api.updateItem(listId, itemId, item)
    }

    override suspend fun deleteItem(listId: String, itemId: String) = withContext(Dispatchers.IO) {
        api.deleteItem(listId, itemId)
    }
}
