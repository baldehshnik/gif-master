package com.vd.study.gif_master.binding.home.repositories

import com.vd.study.core.container.Result
import com.vd.study.data.LocalGifsDataRepository
import com.vd.study.data.RemoteGifsDataRepository
import com.vd.study.gif_master.binding.home.mappers.GifEntityMapper
import com.vd.study.gif_master.binding.home.mappers.LikeAndSaveStatusEntityMapper
import com.vd.study.home.domain.entities.GifEntity
import com.vd.study.home.domain.entities.LikeAndSaveStatusEntity
import com.vd.study.home.domain.repositories.HomeRepository
import kotlinx.coroutines.flow.Flow
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeAdapterRepository @Inject constructor(
    private val remoteRepository: RemoteGifsDataRepository,
    private val localRepository: LocalGifsDataRepository,
    private val gifEntityMapper: GifEntityMapper,
    private val likeAndSaveStatusEntityMapper: LikeAndSaveStatusEntityMapper
) : HomeRepository {

    override suspend fun readGifs(): Result<List<GifEntity>> {
        return remoteRepository.readGifs().suspendMap {
            it.data.map(gifEntityMapper::map)
        }
    }

    override suspend fun readLikeAndSaveStatus(
        accountId: Int,
        gif: GifEntity
    ): Result<LikeAndSaveStatusEntity> {
        return localRepository.readGifByUrl(accountId, gif.url)
            .suspendMap(likeAndSaveStatusEntityMapper::map)
    }

    override suspend fun pagingReadGifs(): Flow<PagingData<GifEntity>> {
        return remoteRepository.pagingReadGifs().map { pagingData ->
            pagingData.map(gifEntityMapper::map)
        }
    }
}