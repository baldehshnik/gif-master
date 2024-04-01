package com.vd.study.viewing.presentation.utils

import androidx.work.Data
import com.vd.study.viewing.domain.entities.GifAuthorEntity
import com.vd.study.viewing.domain.entities.GifEntity

fun Data.getGifEntity(): GifEntity {
    val hasAuthor = getBoolean("hasAuthor", false)
    val author = if (hasAuthor) GifAuthorEntity(
        getString("username") ?: "",
        getString("avatarUrl") ?: ""
    ) else null

    return GifEntity(
        getInt("id", 0),
        getString("title") ?: "",
        getString("url") ?: "",
        author,
        getString("rating") ?: "",
        getBoolean("isLiked", false),
        getBoolean("isSaved", false),
        getBoolean("isViewed", false)
    )
}
