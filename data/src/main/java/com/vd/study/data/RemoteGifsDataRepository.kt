package com.vd.study.data

import androidx.paging.PagingData
import com.vd.study.core.container.Result
import com.vd.study.data.remote.entities.GifsListDataEntity
import com.vd.study.data.remote.entities.RemoteGifDataEntity
import kotlinx.coroutines.flow.Flow

interface RemoteGifsDataRepository {

    suspend fun readGifs(): Result<GifsListDataEntity>

    suspend fun readPopularGifs(): Result<GifsListDataEntity>

    suspend fun pagingReadGifs(): Flow<PagingData<RemoteGifDataEntity>>

    suspend fun pagingReadSearchGifs(query: String): Flow<PagingData<RemoteGifDataEntity>>

}