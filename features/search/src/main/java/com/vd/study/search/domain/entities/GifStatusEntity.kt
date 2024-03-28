package com.vd.study.search.domain.entities

data class GifStatusEntity(
    val gifId: Int,
    val isLiked: Boolean,
    val isSaved: Boolean,
    val isViewed: Boolean
) {

    companion object {
        @JvmStatic
        fun getEmpty(): GifStatusEntity {
            return GifStatusEntity(0, isLiked = false, isSaved = false, isViewed = false)
        }
    }
}
