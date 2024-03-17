package com.vd.study.gif_master.binding.home.mappers

import com.vd.study.viewing.domain.entities.GifEntity
import com.vd.study.core.mapper.Mapper
import com.vd.study.home.domain.entities.FullGifEntity
import javax.inject.Inject

class FullGifEntityToViewingGifEntityMapper @Inject constructor(
    private val gifAuthorEntityToViewingGifAuthorEntityMapper: GifAuthorEntityToViewingGifAuthorEntityMapper
) : Mapper<FullGifEntity, GifEntity> {

    override fun map(input: FullGifEntity): GifEntity {
        return GifEntity(
            input.id,
            input.title,
            input.url,
            gifAuthorEntityToViewingGifAuthorEntityMapper.map(input.author),
            input.rating,
            input.isLiked,
            input.isSaved,
            input.isViewed
        )
    }
}