package com.vd.study.gif_master.binding.home.repositories

import androidx.paging.PagingData
import androidx.paging.map
import com.vd.study.core.container.Result
import com.vd.study.core.global.AccountIdentifier
import com.vd.study.data.LocalGifsDataRepository
import com.vd.study.data.RemoteGifsDataRepository
import com.vd.study.gif_master.binding.home.mappers.GifEntityMapper
import com.vd.study.gif_master.binding.home.mappers.LikeAndSaveStatusEntityMapper
import com.vd.study.gif_master.binding.home.mappers.LocalGifDataEntityMapper
import com.vd.study.home.domain.entities.FullGifEntity
import com.vd.study.home.domain.entities.GifEntity
import com.vd.study.home.domain.entities.LikeAndSaveStatusEntity
import com.vd.study.home.domain.repositories.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeAdapterRepository @Inject constructor(
    private val remoteRepository: RemoteGifsDataRepository,
    private val localRepository: LocalGifsDataRepository,
    private val gifEntityMapper: GifEntityMapper,
    private val likeAndSaveStatusEntityMapper: LikeAndSaveStatusEntityMapper,
    private val localGifDataEntityMapper: LocalGifDataEntityMapper,
    private val accountIdentifier: AccountIdentifier
) : HomeRepository {

    override suspend fun readGifs(): Result<List<GifEntity>> {
        return remoteRepository.readGifs().suspendMap {
            it.data.map(gifEntityMapper::map)
        }
    }

    override suspend fun readLikeAndSaveStatus(gif: GifEntity): Result<LikeAndSaveStatusEntity> {
        return localRepository.readGifByUrl(accountIdentifier.accountIdentifier, gif.url)
            .suspendMap(likeAndSaveStatusEntityMapper::map)
    }

    override suspend fun pagingReadGifs(): Flow<PagingData<GifEntity>> {
        return remoteRepository.pagingReadGifs().map { pagingData ->
            pagingData.map(gifEntityMapper::map)
        }
    }

    override suspend fun updateGif(gif: FullGifEntity): Result<Boolean> {
        return localRepository.updateGif(localGifDataEntityMapper.map(gif))
    }

}