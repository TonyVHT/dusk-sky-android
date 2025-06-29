package com.example.duskskyapp.data.repository.real

import com.example.duskskyapp.data.remote.GameListApi
import com.example.duskskyapp.data.remote.dto.GameListDto
import com.example.duskskyapp.data.remote.dto.GameListItemDto
import com.example.duskskyapp.data.repository.GameListRepository
import javax.inject.Inject

class RealGameListRepository @Inject constructor(
    private val api: GameListApi
) : GameListRepository {

    override suspend fun getListsByUser(userId: String): List<GameListDto> =
        api.getListsByUser(userId)

    override suspend fun getListById(id: String): GameListDto =
        api.getListById(id)

    override suspend fun createList(list: GameListDto): GameListDto =
        api.createList(list)

    override suspend fun updateList(id: String, list: GameListDto) =
        api.updateList(id, list)

    override suspend fun deleteList(id: String) =
        api.deleteList(id)

    override suspend fun getMostRecentLists(): List<GameListDto> =
        api.getMostRecentLists()

    override suspend fun getMostLikedLists(): List<GameListDto> =
        api.getMostLikedLists()

    override suspend fun likeList(id: String) =
        api.likeList(id)

    override suspend fun unlikeList(id: String) =
        api.unlikeList(id)

    override suspend fun getItemsByListId(listId: String): List<GameListItemDto> =
        api.getItemsByListId(listId)

    override suspend fun addItemToList(listId: String, item: GameListItemDto): GameListItemDto =
        api.addItemToList(listId, item)

    override suspend fun updateItem(listId: String, itemId: String, item: GameListItemDto) =
        api.updateItem(listId, itemId, item)

    override suspend fun deleteItem(listId: String, itemId: String) =
        api.deleteItem(listId, itemId)
}
