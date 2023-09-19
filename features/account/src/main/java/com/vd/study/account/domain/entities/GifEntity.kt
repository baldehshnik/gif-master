package com.vd.study.account.domain.entities

data class GifEntity(
    val id: Int,
    val title: String,
    val url: String,
    val rating: String,
    val author: GifAuthorEntity?,
    val isLiked: Boolean,
    val isSaved: Boolean,
    val accountId: Int
)