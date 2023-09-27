package com.vd.study.data

import com.vd.study.core.container.Result
import com.vd.study.data.local.gifs.entities.LocalGifDataEntity
import kotlinx.coroutines.flow.Flow

interface LocalGifsDataRepository {

    suspend fun updateGif(gif: LocalGifDataEntity): Result<Boolean>

    suspend fun readLikedGifsCount(accountId: Int): Result<Int>

    suspend fun readSavedGifsCount(accountId: Int): Result<Int>

    fun readLikedGifs(accountId: Int): Result<Flow<List<LocalGifDataEntity>>>

    fun readSavedGifs(accountId: Int): Result<Flow<List<LocalGifDataEntity>>>

    suspend fun readGifByUrl(accountId: Int, sourceUrl: String): Result<LocalGifDataEntity>

}