package com.vd.study.gif_master.binding.account.mappers

import com.vd.study.account.domain.entities.GifEntity
import com.vd.study.viewing.domain.entities.GifEntity as ViewingGifEntity
import com.vd.study.core.mapper.Mapper
import javax.inject.Inject

class GifEntityToViewingGifEntityMapper @Inject constructor(
    private val gifAuthorEntityToViewingGifAuthorEntityMapper: GifAuthorEntityToViewingGifAuthorEntityMapper
) : Mapper<GifEntity, ViewingGifEntity> {

    override fun map(input: GifEntity): ViewingGifEntity {
        return ViewingGifEntity(
            input.id,
            input.title,
            input.url,
            gifAuthorEntityToViewingGifAuthorEntityMapper.map(input.author),
            input.rating,
            input.isLiked,
            input.isSaved
        )
    }
}