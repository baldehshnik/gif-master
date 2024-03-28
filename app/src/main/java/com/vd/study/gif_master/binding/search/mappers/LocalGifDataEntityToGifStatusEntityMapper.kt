package com.vd.study.gif_master.binding.search.mappers

import com.vd.study.core.mapper.Mapper
import com.vd.study.data.local.gifs.entities.LocalGifDataEntity
import com.vd.study.search.domain.entities.GifStatusEntity
import javax.inject.Inject

class LocalGifDataEntityToGifStatusEntityMapper @Inject constructor() :
    Mapper<LocalGifDataEntity, GifStatusEntity> {

    override fun map(input: LocalGifDataEntity): GifStatusEntity = with(input) {
        return GifStatusEntity(id, isLiked, isSaved, isViewed)
    }
}
