package com.vd.study.data.remote.sources

import androidx.paging.PagingData
import com.vd.study.data.remote.entities.RemoteGifDataEntity
import kotlinx.coroutines.flow.Flow

interface GifsApiDataSourceBehavior {

    suspend fun pagingReadGifs(): Flow<PagingData<RemoteGifDataEntity>>

    suspend fun pagingReadSearchGifs(query: String): Flow<PagingData<RemoteGifDataEntity>>

}
