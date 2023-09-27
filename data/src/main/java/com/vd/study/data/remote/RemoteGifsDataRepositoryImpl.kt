package com.vd.study.data.remote

import com.vd.study.core.dispatchers.IODispatcher
import com.vd.study.core.container.Result
import com.vd.study.data.RemoteGifsDataRepository
import com.vd.study.data.remote.entities.GifsListDataEntity
import com.vd.study.data.exceptions.FailedLoadException
import com.vd.study.data.exceptions.NotFoundException
import com.vd.study.data.exceptions.TimeoutException
import com.vd.study.data.exceptions.UnknownException
import com.vd.study.data.remote.sources.GifsApiDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteGifsDataRepositoryImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val dataSource: GifsApiDataSource
) : RemoteGifsDataRepository {

    override suspend fun readGifs(): Result<GifsListDataEntity> = withContext(ioDispatcher) {
        return@withContext catchExceptionsOf {
            dataSource.readGifs()
        }
    }

    override suspend fun readPopularGifs(): Result<GifsListDataEntity> = withContext(ioDispatcher) {
        return@withContext catchExceptionsOf {
            dataSource.readPopularGifs()
        }
    }

    private inline fun <T> catchExceptionsOf(operation: () -> T): Result<T> {
        return try {
            val popularGifsListDataEntity = operation()
            Result.Correct(popularGifsListDataEntity)
        } catch (timeoutException: TimeoutException) {
            Result.Error(timeoutException)
        } catch (notFoundException: NotFoundException) {
            Result.Error(notFoundException)
        } catch (failedLoadException: FailedLoadException) {
            Result.Error(failedLoadException)
        } catch (unknownException: UnknownException) {
            Result.Error(unknownException)
        }
    }
}