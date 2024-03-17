package com.vd.study.data.local.gifs

import com.vd.study.core.dispatchers.IODispatcher
import com.vd.study.core.container.Result
import com.vd.study.data.LocalGifsDataRepository
import com.vd.study.data.exceptions.FailedUpdateException
import com.vd.study.data.local.catchReadingExceptionOf
import com.vd.study.data.local.executeDatabaseUpdating
import com.vd.study.data.local.gifs.entities.LocalGifDataEntity
import com.vd.study.data.local.gifs.sources.LocalGifsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalGifsDataRepositoryImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val gifsDao: LocalGifsDao
) : LocalGifsDataRepository {

    override suspend fun updateGif(gif: LocalGifDataEntity): Result<Boolean> =
        withContext(ioDispatcher) {
            return@withContext executeDatabaseUpdating(
                operation = {
                    gifsDao.updateOrInsert(gif, ioDispatcher)
                }, error = Result.Error(FailedUpdateException())
            )
        }

    override suspend fun readLikedGifsCount(accountId: Int): Result<Int> =
        withContext(ioDispatcher) {
            return@withContext catchReadingExceptionOf {
                gifsDao.readLikedGifsCount(accountId)
            }
        }

    override suspend fun readSavedGifsCount(accountId: Int): Result<Int> =
        withContext(ioDispatcher) {
            return@withContext catchReadingExceptionOf {
                gifsDao.readSavedGifsCount(accountId)
            }
        }

    override fun readLikedGifs(accountId: Int): Result<Flow<List<LocalGifDataEntity>>> {
        return catchReadingExceptionOf {
            gifsDao.readLikedGifs(accountId)
        }
    }

    override fun readViewedGifs(accountId: Int): Result<Flow<List<LocalGifDataEntity>>> {
        return catchReadingExceptionOf {
            gifsDao.readViewedGifs(accountId)
        }
    }

    override fun readSavedGifs(accountId: Int): Result<Flow<List<LocalGifDataEntity>>> {
        return catchReadingExceptionOf {
            gifsDao.readSavedGifs(accountId)
        }
    }

    override suspend fun readGifByUrl(
        accountId: Int,
        sourceUrl: String
    ): Result<LocalGifDataEntity> = withContext(ioDispatcher) {
        return@withContext catchReadingExceptionOf {
            gifsDao.readGifByUrl(accountId, sourceUrl)
        }
    }
}
