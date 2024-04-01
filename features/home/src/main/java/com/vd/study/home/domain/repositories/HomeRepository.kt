package com.vd.study.home.domain.repositories

import androidx.paging.PagingData
import com.vd.study.core.container.Result
import com.vd.study.home.domain.entities.GifEntity
import com.vd.study.home.domain.entities.LikeAndSaveStatusEntity
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    suspend fun readLikeAndSaveStatus(gif: GifEntity): Result<LikeAndSaveStatusEntity>

    suspend fun pagingReadGifs(): Flow<PagingData<GifEntity>>

}
