package com.vd.study.data.remote.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vd.study.core.dispatchers.IODispatcher
import com.vd.study.data.exceptions.UnknownException
import com.vd.study.data.remote.entities.RemoteGifDataEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchGifPagingDataSource@Inject constructor(
    private val gifsApiService: GifsApiService,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val query: String
) : PagingSource<Int, RemoteGifDataEntity>() {

    override fun getRefreshKey(state: PagingState<Int, RemoteGifDataEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val currentOffset = state.closestPageToPosition(anchorPosition)
            currentOffset?.prevKey?.plus(MAX_ONCE_LOADING_GIFS_COUNT)
                ?: currentOffset?.nextKey?.minus(MAX_ONCE_LOADING_GIFS_COUNT)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RemoteGifDataEntity> =
        withContext(ioDispatcher) {
            val offset = params.key ?: 0
            try {
                val call = gifsApiService.readSearchGifs(query, offset)
                val response = call.execute()

                val data = if (response.isSuccessful) {
                    response.body()?.data ?: listOf()
                } else {
                    return@withContext LoadResult.Error(getExceptionByCode(response.code()))
                }

                LoadResult.Page(
                    data = data,
                    prevKey = if (offset <= 0) null else offset - MAX_ONCE_LOADING_GIFS_COUNT,
                    nextKey = if (offset > MAX_ELEMENTS_COUNT) null else offset + MAX_ONCE_LOADING_GIFS_COUNT
                )
            } catch (e: Exception) {
                LoadResult.Error(UnknownException())
            }
        }
}
