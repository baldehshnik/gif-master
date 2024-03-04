package com.vd.study.gif_master.binding.viewing.mappers

import com.vd.study.viewing.domain.entities.GifEntity
import com.vd.study.core.global.AccountIdentifier
import com.vd.study.core.mapper.Mapper
import com.vd.study.data.local.gifs.entities.LocalGifDataEntity
import javax.inject.Inject

class LocalGifDataEntityMapper @Inject constructor(
    private val localGifAuthorDataEntityMapper: LocalGifAuthorDataEntityMapper,
    private val accountIdentifier: AccountIdentifier
) : Mapper<GifEntity, LocalGifDataEntity> {

    override fun map(input: GifEntity): LocalGifDataEntity {
        return LocalGifDataEntity(
            id = input.id,
            title = input.title,
            rating = input.url,
            author = localGifAuthorDataEntityMapper.map(input.author),
            url = input.url,
            isLiked = input.isLiked,
            isSaved = input.isSaved,
            accountId = accountIdentifier.accountIdentifier
        )
    }
}
