package com.vd.study.gif_master.binding.home.mappers

import com.vd.study.core.mapper.Mapper
import javax.inject.Inject
import com.vd.study.home.domain.entities.GifAuthorEntity as HomeGifAuthorEntity
import com.vd.study.viewing.domain.entities.GifAuthorEntity as ViewingGifAuthorEntity

class GifAuthorEntityToViewingGifAuthorEntityMapper @Inject constructor() :
    Mapper<HomeGifAuthorEntity?, ViewingGifAuthorEntity?> {

    override fun map(input: HomeGifAuthorEntity?): ViewingGifAuthorEntity? {
        return if (input == null) null
        else ViewingGifAuthorEntity(
            input.username,
            input.avatarUrl
        )
    }
}