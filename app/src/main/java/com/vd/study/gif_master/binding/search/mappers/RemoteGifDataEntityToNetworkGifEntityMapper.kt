package com.vd.study.gif_master.binding.search.mappers

import com.vd.study.core.mapper.Mapper
import com.vd.study.data.remote.entities.RemoteGifDataEntity
import com.vd.study.search.domain.entities.NetworkGifEntity
import javax.inject.Inject

class RemoteGifDataEntityToNetworkGifEntityMapper @Inject constructor(
    private val gifAuthorDataEntityMapper: GifAuthorDataEntityToGifAuthorEntityMapper
) : Mapper<RemoteGifDataEntity, NetworkGifEntity> {

    override fun map(input: RemoteGifDataEntity): NetworkGifEntity = with(input) {
        return NetworkGifEntity(
            id, title, images.original.url, gifAuthorDataEntityMapper.map(author), rating
        )
    }
}
