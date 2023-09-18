package com.vd.study.data.local.gifs

import com.vd.study.core.Result
import com.vd.study.data.LocalGifsDataRepository
import com.vd.study.data.exceptions.FailedUpdateException
import com.vd.study.data.exceptions.UnknownException
import com.vd.study.data.local.executeDatabaseUpdating
import com.vd.study.data.local.gifs.entities.LocalGifDataEntity
import com.vd.study.data.local.gifs.sources.LocalGifsDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalGifsDataRepositoryImpl @Inject constructor(
    private val gifsDao: LocalGifsDao
) : LocalGifsDataRepository {

    override suspend fun updateGif(gif: LocalGifDataEntity): Result<Boolean> {
        return executeDatabaseUpdating(
            operation = {
                gifsDao.updateOrInsert(gif)
            },
            Result.Error(FailedUpdateException())
        )
    }

    override suspend fun readLikedGifsCount(accountId: Int): Result<Int> {
        return catchExceptionOf {
            gifsDao.readLikedGifsCount(accountId)
        }
    }

    override suspend fun readSavedGifsCount(accountId: Int): Result<Int> {
        return catchExceptionOf {
            gifsDao.readSavedGifsCount(accountId)
        }
    }

    override fun readLikedGifs(accountId: Int): Result<Flow<List<LocalGifDataEntity>>> {
        return catchExceptionOf {
            gifsDao.readLikedGifs(accountId)
        }
    }

    override fun readSavedGifs(accountId: Int): Result<Flow<List<LocalGifDataEntity>>> {
        return catchExceptionOf {
            gifsDao.readSavedGifs(accountId)
        }
    }

    private inline fun <T> catchExceptionOf(operation: () -> T): Result<T> {
        return try {
            Result.Correct(operation())
        } catch (e: Exception) {
            Result.Error(UnknownException())
        }
    }
}