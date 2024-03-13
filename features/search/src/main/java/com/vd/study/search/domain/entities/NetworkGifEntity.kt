package com.vd.study.search.domain.entities

data class NetworkGifEntity(
    val id: String,
    val title: String,
    val url: String,
    val author: GifAuthorEntity?,
    val rating: String
)
