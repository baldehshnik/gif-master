package com.vd.study.home.domain.entities

data class LikeAndSaveStatusEntity(
    val gifId: Int,
    val isLiked: Boolean,
    val isSaved: Boolean,
    val isViewed: Boolean
)