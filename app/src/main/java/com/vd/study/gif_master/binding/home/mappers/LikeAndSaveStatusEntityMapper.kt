package com.vd.study.gif_master.binding.home.mappers

import com.vd.study.core.mapper.Mapper
import com.vd.study.data.local.gifs.entities.LocalGifDataEntity
import com.vd.study.home.domain.entities.LikeAndSaveStatusEntity
import javax.inject.Inject

class LikeAndSaveStatusEntityMapper @Inject constructor() :
    Mapper<LocalGifDataEntity, LikeAndSaveStatusEntity> {

    override fun map(input: LocalGifDataEntity): LikeAndSaveStatusEntity {
        return LikeAndSaveStatusEntity(input.id, input.isLiked, input.isSaved, input.isViewed)
    }
}