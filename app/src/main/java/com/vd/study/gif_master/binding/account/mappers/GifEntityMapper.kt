package com.vd.study.gif_master.binding.account.mappers

import com.vd.study.account.domain.entities.GifEntity
import com.vd.study.data.local.gifs.entities.LocalGifDataEntity
import com.vd.study.core.mapper.Mapper
import javax.inject.Inject

class GifEntityMapper @Inject constructor(
    private val authorEntityMapper: AuthorEntityMapper
) : Mapper<LocalGifDataEntity, GifEntity> {

    override fun map(input: LocalGifDataEntity): GifEntity {
        return GifEntity(
            input.id,
            input.title,
            input.url,
            input.rating,
            authorEntityMapper.map(input.author),
            input.isLiked,
            input.isSaved,
            input.accountId
        )
    }
}