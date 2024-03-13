package com.vd.study.gif_master.binding.search.mappers

import com.vd.study.core.mapper.Mapper
import com.vd.study.data.remote.entities.GifAuthorDataEntity
import com.vd.study.search.domain.entities.GifAuthorEntity
import javax.inject.Inject

class GifAuthorDataEntityToGifAuthorEntityMapper @Inject constructor() :
    Mapper<GifAuthorDataEntity?, GifAuthorEntity?> {

    override fun map(input: GifAuthorDataEntity?): GifAuthorEntity? {
        return if (input == null) null else GifAuthorEntity(input.username, input.avatarUrl)
    }
}