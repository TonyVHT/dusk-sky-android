package com.example.duskskyapp.data.remote

import com.example.duskskyapp.data.remote.dto.GameListDto
import com.example.duskskyapp.data.remote.dto.GameListItemDto
import retrofit2.http.*

interface GameListApi {

    @GET("/lists/user/{userId}")
    suspend fun getListsByUser(@Path("userId") userId: String): List<GameListDto>

    @GET("/lists/{id}")
    suspend fun getListById(@Path("id") id: String): GameListDto

    @POST("/lists")
    suspend fun createList(@Body list: GameListDto): GameListDto

    @PUT("/lists/{id}")
    suspend fun updateList(@Path("id") id: String, @Body list: GameListDto)

    @DELETE("/lists/{id}")
    suspend fun deleteList(@Path("id") id: String)

    @GET("/lists/recent")
    suspend fun getMostRecentLists(): List<GameListDto>

    @GET("/lists/popular")
    suspend fun getMostLikedLists(): List<GameListDto>

    @PUT("/lists/like/{id}")
    suspend fun likeList(@Path("id") id: String)

    @PUT("/lists/unlike/{id}")
    suspend fun unlikeList(@Path("id") id: String)

    @GET("/lists/{listId}/items")
    suspend fun getItemsByListId(@Path("listId") listId: String): List<GameListItemDto>

    @POST("/lists/{listId}/items")
    suspend fun addItemToList(@Path("listId") listId: String, @Body item: GameListItemDto): GameListItemDto

    @PUT("/lists/{listId}/items/{itemId}")
    suspend fun updateItem(
        @Path("listId") listId: String,
        @Path("itemId") itemId: String,
        @Body item: GameListItemDto
    )

    @DELETE("/lists/{listId}/items/{itemId}")
    suspend fun deleteItem(
        @Path("listId") listId: String,
        @Path("itemId") itemId: String
    )
}
