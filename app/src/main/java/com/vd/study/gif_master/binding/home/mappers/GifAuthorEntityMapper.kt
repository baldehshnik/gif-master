package com.vd.study.gif_master.binding.home.mappers

import com.vd.study.core.mapper.Mapper
import com.vd.study.data.remote.entities.GifAuthorDataEntity
import com.vd.study.home.domain.entities.GifAuthorEntity

class GifAuthorEntityMapper : Mapper<GifAuthorDataEntity?, GifAuthorEntity?> {

    override fun map(input: GifAuthorDataEntity?): GifAuthorEntity? {
        if (input == null) return null
        return GifAuthorEntity(input.username, input.avatarUrl)
    }
}