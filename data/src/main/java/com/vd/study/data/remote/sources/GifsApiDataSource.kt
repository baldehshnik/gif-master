package com.vd.study.data.remote.sources

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vd.study.core.dispatchers.IODispatcher
import com.vd.study.data.exceptions.NotFoundException
import com.vd.study.data.exceptions.TimeoutException
import com.vd.study.data.exceptions.UnknownException
import com.vd.study.data.remote.entities.RemoteGifDataEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

fun getExceptionByCode(code: Int): Exception {
    return when (code) {
        404 -> NotFoundException()
        408 -> TimeoutException()
        else -> UnknownException()
    }
}

class GifsApiDataSource @Inject constructor(
    private val gifsApiService: GifsApiService,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : GifsApiDataSourceBehavior {

    override suspend fun pagingReadGifs(): Flow<PagingData<RemoteGifDataEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = MAX_ONCE_LOADING_GIFS_COUNT,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GifsPagingDataSource(gifsApiService, ioDispatcher)
            }
        ).flow
    }

    override suspend fun pagingReadSearchGifs(query: String): Flow<PagingData<RemoteGifDataEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = MAX_ONCE_LOADING_GIFS_COUNT,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchGifPagingDataSource(gifsApiService, ioDispatcher, query)
            }
        ).flow
    }
}
