package com.vd.study.gif_master.binding.account.mappers

import com.vd.study.account.domain.entities.GifAuthorEntity
import com.vd.study.data.local.gifs.entities.LocalGifAuthorDataEntity
import com.vd.study.core.mapper.Mapper
import javax.inject.Inject

class AuthorEntityMapper @Inject constructor() : Mapper<LocalGifAuthorDataEntity?, GifAuthorEntity?> {

    override fun map(input: LocalGifAuthorDataEntity?): GifAuthorEntity? {
        return if (input == null) null else GifAuthorEntity(input.username, input.avatarUrl)
    }
}
