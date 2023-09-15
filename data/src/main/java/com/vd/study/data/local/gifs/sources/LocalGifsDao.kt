package com.vd.study.data.local.gifs.sources

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vd.study.data.local.LocalDatabaseCore
import com.vd.study.data.local.gifs.entities.LocalGifDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalGifsDao {

    @Insert
    suspend fun writeGif(gif: LocalGifDataEntity): Long

    @Update
    suspend fun updateGif(gif: LocalGifDataEntity): Int

    @Query("SELECT * FROM ${LocalDatabaseCore.GIFS_TABLE_NAME} WHERE account_id = :accountId AND is_liked = 1")
    fun readLikedGifs(accountId: Int): Flow<List<LocalGifDataEntity>>

    @Query("SELECT * FROM ${LocalDatabaseCore.GIFS_TABLE_NAME} WHERE account_id = :accountId AND is_saved = 1")
    fun readSavedGifs(accountId: Int): Flow<List<LocalGifDataEntity>>

    @Query("SELECT COUNT(*) FROM ${LocalDatabaseCore.GIFS_TABLE_NAME} WHERE account_id = :accountId AND is_liked = 1")
    suspend fun readLikedGifsCount(accountId: Int): Int

    @Query("SELECT COUNT(*) FROM ${LocalDatabaseCore.GIFS_TABLE_NAME} WHERE account_id = :accountId AND is_saved = 1")
    suspend fun readSavedGifsCount(accountId: Int): Int

    @Query("SELECT * FROM ${LocalDatabaseCore.GIFS_TABLE_NAME} WHERE id = :id LIMIT 1")
    suspend fun readGifById(id: Int): LocalGifDataEntity?

    suspend fun insertOrReplace(gif: LocalGifDataEntity): Long {
        val insertedGif: LocalGifDataEntity? = readGifById(gif.id)
        return if (insertedGif == null) {
            writeGif(gif)
        } else {
            updateGif(gif).toLong()
        }
    }
}