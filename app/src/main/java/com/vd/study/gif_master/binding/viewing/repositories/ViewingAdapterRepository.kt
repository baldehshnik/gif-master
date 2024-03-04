package com.vd.study.gif_master.binding.viewing.repositories

import com.vd.study.viewing.domain.entities.GifEntity
import com.vd.study.viewing.domain.repositories.ViewingRepository
import com.vd.study.core.container.Result
import com.vd.study.data.LocalGifsDataRepository
import com.vd.study.gif_master.binding.viewing.mappers.LocalGifDataEntityMapper
import javax.inject.Inject

class ViewingAdapterRepository @Inject constructor(
    private val localRepository: LocalGifsDataRepository,
    private val localGifDataEntityMapper: LocalGifDataEntityMapper
) : ViewingRepository {

    override suspend fun updateGif(gif: GifEntity): Result<Boolean> {
        return localRepository.updateGif(localGifDataEntityMapper.map(gif))
    }
}