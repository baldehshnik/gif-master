package com.vd.study.gif_master.binding.search.mappers

import com.vd.study.core.mapper.Mapper
import javax.inject.Inject
import com.vd.study.search.domain.entities.GifAuthorEntity as SearchGifAuthorEntity
import com.vd.study.viewing.domain.entities.GifAuthorEntity as ViewingGifAuthorEntity

class SearchGifAuthorEntityToViewingGifAuthorEntityMapper @Inject constructor() :
    Mapper<SearchGifAuthorEntity?, ViewingGifAuthorEntity?> {

    override fun map(input: SearchGifAuthorEntity?): ViewingGifAuthorEntity? {
        if (input == null) return null
        return ViewingGifAuthorEntity(input.username, input.avatarUrl)
    }
}
