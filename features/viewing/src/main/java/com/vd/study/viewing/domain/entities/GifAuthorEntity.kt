package com.vd.study.viewing.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GifAuthorEntity(
    val username: String,
    val avatarUrl: String
): Parcelable
