package com.vd.study.data.remote.sources

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vd.study.data.remote.entities.GifsListDataEntity
import com.vd.study.data.exceptions.FailedLoadException
import com.vd.study.data.exceptions.NotFoundException
import com.vd.study.data.exceptions.TimeoutException
import com.vd.study.data.exceptions.UnknownException
import com.vd.study.data.remote.entities.RemoteGifDataEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import javax.inject.Inject

fun getExceptionByCode(code: Int): Exception {
    return when (code) {
        404 -> NotFoundException()
        408 -> TimeoutException()
        else -> UnknownException()
    }
}

class GifsApiDataSource @Inject constructor(
    private val gifsApiService: GifsApiService
) : GifsApiDataSourceBehavior {

    override suspend fun readGifs(): GifsListDataEntity {
        return executeReadingWithCall(gifsApiService.getAllGifs())
    }

    override suspend fun readPopularGifs(): GifsListDataEntity {
        return executeReadingWithCall(gifsApiService.getAllPopularGifs())
    }

    override suspend fun pagingReadGifs(): Flow<PagingData<RemoteGifDataEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = MAX_ONCE_LOADING_GIFS_COUNT,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GifsPagingDataSource(gifsApiService)
            }
        ).flow
    }

    private fun <T> executeReadingWithCall(call: Call<T>): T {
        val response = try {
            call.execute()
        } catch (e: Exception) {
            throw UnknownException()
        }

        if (response.isSuccessful) {
            return response.body() ?: throw FailedLoadException()
        } else {
            throw getExceptionByCode(response.code())
        }
    }
}