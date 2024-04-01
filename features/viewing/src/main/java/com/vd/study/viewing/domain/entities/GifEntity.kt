package com.vd.study.viewing.domain.entities

import android.os.Parcelable
import androidx.work.Data
import kotlinx.parcelize.Parcelize

@Parcelize
data class GifEntity(
    val id: Int,
    val title: String,
    val url: String,
    val author: GifAuthorEntity?,
    val rating: String,
    val isLiked: Boolean,
    val isSaved: Boolean,
    val isViewed: Boolean
) : Parcelable {

    fun createWorkData(): Data {
        val data = Data.Builder()
            .putInt("id", id)
            .putString("title", title)
            .putString("url", url)
            .putBoolean("hasAuthor", author != null)
            .putString("rating", rating)
            .putBoolean("isLiked", isLiked)
            .putBoolean("isSaved", isSaved)
            .putBoolean("isViewed", isViewed)

        if (author != null) {
            data.putString("username", author.username).putString("avatarUrl", author.avatarUrl)
        }

        return data.build()
    }
}
