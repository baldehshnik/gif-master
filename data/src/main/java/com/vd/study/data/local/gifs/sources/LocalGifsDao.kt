package com.vd.study.data.local.gifs.sources

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vd.study.data.local.LocalDatabaseCore
import com.vd.study.data.local.gifs.entities.LocalGifDataEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Dao
interface LocalGifsDao {

    @Insert
    suspend fun writeGif(gif: LocalGifDataEntity): Long

    @Update
    suspend fun updateGif(gif: LocalGifDataEntity): Int

    @Query("DELETE FROM ${LocalDatabaseCore.GIFS_TABLE_NAME} WHERE account_id = :accountId AND id = :id")
    suspend fun removeGif(accountId: Int, id: Int): Int

    @Query("SELECT * FROM ${LocalDatabaseCore.GIFS_TABLE_NAME} WHERE account_id = :accountId AND is_liked = 1 ORDER BY id DESC")
    fun readLikedGifs(accountId: Int): Flow<List<LocalGifDataEntity>>

    @Query("SELECT * FROM ${LocalDatabaseCore.GIFS_TABLE_NAME} WHERE account_id = :accountId AND is_viewed = 1 ORDER BY id DESC")
    fun readViewedGifs(accountId: Int): Flow<List<LocalGifDataEntity>>

    @Query("SELECT * FROM ${LocalDatabaseCore.GIFS_TABLE_NAME} WHERE account_id = :accountId AND is_saved = 1 ORDER BY id DESC")
    fun readSavedGifs(accountId: Int): Flow<List<LocalGifDataEntity>>

    @Query("SELECT COUNT(*) FROM ${LocalDatabaseCore.GIFS_TABLE_NAME} WHERE account_id = :accountId AND is_liked = 1")
    suspend fun readLikedGifsCount(accountId: Int): Int

    @Query("SELECT COUNT(*) FROM ${LocalDatabaseCore.GIFS_TABLE_NAME} WHERE account_id = :accountId AND is_saved = 1")
    suspend fun readSavedGifsCount(accountId: Int): Int

    @Query("SELECT * FROM ${LocalDatabaseCore.GIFS_TABLE_NAME} WHERE id = :id LIMIT 1")
    suspend fun readGifById(id: Int): LocalGifDataEntity?

    @Query("SELECT * FROM ${LocalDatabaseCore.GIFS_TABLE_NAME} WHERE account_id = :accountId AND source_url = :url LIMIT 1")
    suspend fun readGifByUrl(accountId: Int, url: String): LocalGifDataEntity

    @Query("SELECT * FROM ${LocalDatabaseCore.GIFS_TABLE_NAME} WHERE account_id =:accountId AND source_url = :url LIMIT 1")
    suspend fun hasGif(accountId: Int, url: String): LocalGifDataEntity?

    @Query(
        "UPDATE ${LocalDatabaseCore.GIFS_TABLE_NAME} SET is_viewed = 0 WHERE account_id = :accountId AND id IN (" +
                "SELECT id FROM ${LocalDatabaseCore.GIFS_TABLE_NAME} WHERE account_id = :accountId " +
                "ORDER BY id ASC LIMIT 200)"
    )
    suspend fun clearViewedGifs(accountId: Int)

    @Query(
        "DELETE FROM ${LocalDatabaseCore.GIFS_TABLE_NAME} " +
                "WHERE account_id = :accountId AND is_viewed = 0 AND is_liked = 0 AND is_saved = 0"
    )
    suspend fun clearDatabaseFromCache(accountId: Int)

    @Query("SELECT COUNT(*) FROM ${LocalDatabaseCore.GIFS_TABLE_NAME} WHERE account_id = :accountId AND is_viewed = 1")
    suspend fun readViewedGifsCount(accountId: Int): Int

    suspend fun updateOrInsert(gif: LocalGifDataEntity, ioDispatcher: CoroutineDispatcher): Long {
        val insertedGif = hasGif(gif.accountId, gif.url)
        return if (insertedGif == null) {
            val result = writeGif(gif.copy(id = 0))
            clearDatabase(ioDispatcher, gif.accountId)
            result
        } else {
            removeGif(insertedGif.accountId, insertedGif.id)
            writeGif(gif.copy(id = 0))
        }
    }

    private suspend fun clearDatabase(ioDispatcher: CoroutineDispatcher, accountId: Int) {
        withContext(ioDispatcher) {
            launch(ioDispatcher) {
                val count = readViewedGifsCount(accountId)
                if (count >= 500) {
                    clearViewedGifs(accountId)
                    clearDatabaseFromCache(accountId)
                }
            }
        }
    }
}
