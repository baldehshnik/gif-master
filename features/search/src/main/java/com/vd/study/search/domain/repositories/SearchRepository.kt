package com.vd.study.search.domain.repositories

import androidx.paging.PagingData
import com.vd.study.core.container.Result
import com.vd.study.search.domain.entities.GifStatusEntity
import com.vd.study.search.domain.entities.NetworkGifEntity
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun pagingReadSearchGifs(query: String): Flow<PagingData<NetworkGifEntity>>

    suspend fun readGifStatus(entity: NetworkGifEntity): Result<GifStatusEntity>

}
