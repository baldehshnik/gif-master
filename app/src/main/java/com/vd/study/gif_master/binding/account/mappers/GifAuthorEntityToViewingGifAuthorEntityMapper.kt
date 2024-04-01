package com.vd.study.gif_master.binding.account.mappers

import com.vd.study.account.domain.entities.GifAuthorEntity
import com.vd.study.core.mapper.Mapper
import javax.inject.Inject
import com.vd.study.viewing.domain.entities.GifAuthorEntity as ViewingGifAuthorEntity

class GifAuthorEntityToViewingGifAuthorEntityMapper @Inject constructor() :
    Mapper<GifAuthorEntity?, ViewingGifAuthorEntity?> {

    override fun map(input: GifAuthorEntity?): ViewingGifAuthorEntity? {
        return if (input == null) null
        else ViewingGifAuthorEntity(input.username, input.avatarUrl)
    }
}
