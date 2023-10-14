package com.vd.study.data.remote.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vd.study.data.exceptions.UnknownException
import com.vd.study.data.remote.entities.RemoteGifDataEntity
import javax.inject.Inject

class GifsPagingDataSource @Inject constructor(
    private val gifsApiService: GifsApiService
) : PagingSource<Int, RemoteGifDataEntity>() {

    override fun getRefreshKey(state: PagingState<Int, RemoteGifDataEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val currentOffset = state.closestPageToPosition(anchorPosition)
            currentOffset?.prevKey?.plus(MAX_ONCE_LOADING_GIFS_COUNT) ?: currentOffset?.nextKey?.minus(MAX_ONCE_LOADING_GIFS_COUNT)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RemoteGifDataEntity> {
        val offset = params.key ?: 0
        return try {
            val call = gifsApiService.readGifs(offset)
            val response = call.execute()

            val data = if (response.isSuccessful) {
                response.body()?.data ?: listOf()
            } else {
                return LoadResult.Error(getExceptionByCode(response.code()))
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