package com.vd.study.home.domain.entities

data class GifEntity(
    val id: String,
    val title: String,
    val url: String,
    val width: Int,
    val height: Int,
    val author: GifAuthorEntity?,
    val rating: String
)