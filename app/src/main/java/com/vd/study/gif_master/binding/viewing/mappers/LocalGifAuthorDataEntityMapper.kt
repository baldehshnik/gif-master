package com.vd.study.gif_master.binding.viewing.mappers

import com.vd.study.viewing.domain.entities.GifAuthorEntity
import com.vd.study.core.mapper.Mapper
import com.vd.study.data.local.gifs.entities.LocalGifAuthorDataEntity

class LocalGifAuthorDataEntityMapper : Mapper<GifAuthorEntity?, LocalGifAuthorDataEntity?> {

    override fun map(input: GifAuthorEntity?): LocalGifAuthorDataEntity? {
        return if (input == null) null else LocalGifAuthorDataEntity(
            username = input.username,
            avatarUrl = input.avatarUrl
        )
    }
}