package com.vd.study.home.domain.entities

data class FullGifEntity(
    val id: Int,
    val title: String,
    val url: String,
    val width: Int,
    val height: Int,
    val author: GifAuthorEntity?,
    val rating: String,
    val isLiked: Boolean,
    val isSaved: Boolean,
    val isViewed: Boolean
) {

    companion object {

        @JvmStatic
        fun getInstateWith(gifEntity: GifEntity, status: LikeAndSaveStatusEntity): FullGifEntity {
            return FullGifEntity(
                id = status.gifId,
                title = gifEntity.title,
                url = gifEntity.url,
                width = gifEntity.width,
                height = gifEntity.height,
                author = gifEntity.author,
                rating = gifEntity.rating,
                isLiked = status.isLiked,
                isSaved = status.isSaved,
                isViewed = status.isViewed
            )
        }
    }
}
