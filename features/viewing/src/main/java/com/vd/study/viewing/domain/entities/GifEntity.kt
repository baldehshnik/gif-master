package com.vd.study.viewing.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GifEntity(
    val id: Int,
    val title: String,
    val url: String,
    val author: GifAuthorEntity?,
    val rating: String,
    val isLiked: Boolean,
    val isSaved: Boolean
) : Parcelable