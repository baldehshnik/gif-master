package com.vd.study.data.remote

import com.vd.study.core.Result
import com.vd.study.data.RemoteGifsDataRepository
import com.vd.study.data.remote.entities.GifsListDataEntity
import com.vd.study.data.exceptions.FailedLoadException
import com.vd.study.data.exceptions.NotFoundException
import com.vd.study.data.exceptions.TimeoutException
import com.vd.study.data.exceptions.UnknownException
import com.vd.study.data.remote.sources.GifsApiDataSource
import javax.inject.Inject

class RemoteGifsDataRepositoryImpl @Inject constructor(
    private val dataSource: GifsApiDataSource
) : RemoteGifsDataRepository {

    override suspend fun readGifs(): Result<GifsListDataEntity> {
        return catchExceptionsOf {
            dataSource.readGifs()
        }
    }

    override suspend fun readPopularGifs(): Result<GifsListDataEntity> {
        return catchExceptionsOf {
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