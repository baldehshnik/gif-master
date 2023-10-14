package com.vd.study.home.domain.entities

data class FullGifEntity(
    val id: String,
    val title: String,
    val url: String,
    val author: GifAuthorEntity?,
    val rating: String,
    val isLiked: Boolean,
    val isSaved: Boolean
) {

    companion object {

        @JvmStatic
        fun getInstateWith(gifEntity: GifEntity, status: LikeAndSaveStatusEntity): FullGifEntity {
            return FullGifEntity(
                id = gifEntity.id,
                title = gifEntity.title,
                url = gifEntity.url,
                author = gifEntity.author,
                rating = gifEntity.rating,
                isLiked = status.isLiked,
                isSaved = status.isSaved
            )
        }
    }
}