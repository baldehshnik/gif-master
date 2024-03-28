package com.vd.study.search.domain.entities

data class GifEntity(
    val id: Int,
    val title: String,
    val url: String,
    val author: GifAuthorEntity?,
    val rating: String,
    val isLiked: Boolean,
    val isSaved: Boolean,
    val isViewed: Boolean
) {

    companion object {
        @JvmStatic
        fun getEntity(gifEntity: NetworkGifEntity, statusEntity: GifStatusEntity): GifEntity {
            return GifEntity(
                id = statusEntity.gifId,
                title = gifEntity.title,
                url = gifEntity.url,
                author = gifEntity.author,
                rating = gifEntity.rating,
                isLiked = statusEntity.isLiked,
                isSaved = statusEntity.isSaved,
                isViewed = statusEntity.isViewed
            )
        }
    }
}
