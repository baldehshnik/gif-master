package com.vd.study.gif_master.binding.search.mappers

import com.vd.study.core.mapper.Mapper
import javax.inject.Inject
import com.vd.study.search.domain.entities.GifEntity as SearchGifEntity
import com.vd.study.viewing.domain.entities.GifEntity as ViewingGifEntity

class SearchGifEntityToViewingGifEntityMapper @Inject constructor(
    private val searchGifAuthorEntityToViewingGifAuthorEntityMapper: SearchGifAuthorEntityToViewingGifAuthorEntityMapper
) : Mapper<SearchGifEntity, ViewingGifEntity> {

    override fun map(input: SearchGifEntity): ViewingGifEntity = with(input) {
        return ViewingGifEntity(
            id,
            title,
            url,
            searchGifAuthorEntityToViewingGifAuthorEntityMapper.map(author),
            rating,
            isLiked,
            isSaved,
            isViewed
        )
    }
}
