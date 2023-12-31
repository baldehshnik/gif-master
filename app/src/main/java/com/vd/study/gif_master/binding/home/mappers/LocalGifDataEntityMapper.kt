package com.vd.study.gif_master.binding.home.mappers

import com.vd.study.core.global.AccountIdentifier
import com.vd.study.core.mapper.Mapper
import com.vd.study.data.local.gifs.entities.LocalGifDataEntity
import com.vd.study.home.domain.entities.FullGifEntity
import javax.inject.Inject

class LocalGifDataEntityMapper @Inject constructor(
    private val localGifAuthorDataEntityMapper: LocalGifAuthorDataEntityMapper,
    private val accountIdentifier: AccountIdentifier
) : Mapper<FullGifEntity, LocalGifDataEntity> {

    override fun map(input: FullGifEntity): LocalGifDataEntity {
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