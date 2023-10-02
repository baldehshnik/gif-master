package com.vd.study.gif_master.binding.home.mappers

import com.vd.study.core.mapper.Mapper
import com.vd.study.data.remote.entities.RemoteGifDataEntity
import com.vd.study.home.domain.entities.GifEntity

class GifEntityMapper(
    private val gifAuthorEntityMapper: GifAuthorEntityMapper
) : Mapper<RemoteGifDataEntity, GifEntity> {

    override fun map(input: RemoteGifDataEntity): GifEntity {
        return GifEntity(
            input.id,
            input.title,
            input.url,
            gifAuthorEntityMapper.map(input.author),
            input.rating
        )
    }
}