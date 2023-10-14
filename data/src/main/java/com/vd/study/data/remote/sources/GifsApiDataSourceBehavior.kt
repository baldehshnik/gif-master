package com.vd.study.data.remote.sources

import androidx.paging.PagingData
import com.vd.study.data.remote.entities.GifsListDataEntity
import com.vd.study.data.remote.entities.RemoteGifDataEntity
import kotlinx.coroutines.flow.Flow

interface GifsApiDataSourceBehavior {

    suspend fun readGifs(): GifsListDataEntity

    suspend fun readPopularGifs(): GifsListDataEntity

    suspend fun pagingReadGifs(): Flow<PagingData<RemoteGifDataEntity>>

}