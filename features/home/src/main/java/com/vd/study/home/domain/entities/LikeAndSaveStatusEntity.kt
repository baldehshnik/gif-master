package com.vd.study.home.domain.entities

data class LikeAndSaveStatusEntity(
    val gifId: Int,
    val isLiked: Boolean,
    val isSaved: Boolean,
    val isViewed: Boolean
) {

    companion object {

        @JvmStatic
        fun getEmpty(): LikeAndSaveStatusEntity {
            return LikeAndSaveStatusEntity(
                gifId = 0, isLiked = false, isSaved = false, isViewed = false
            )
        }
    }
}
