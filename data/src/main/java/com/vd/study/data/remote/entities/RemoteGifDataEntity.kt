package com.vd.study.data.remote.entities

import com.google.gson.annotations.SerializedName

data class RemoteGifDataEntity(

    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("images")
    val images: GifImageDataEntity,

    @SerializedName("user")
    val author: GifAuthorDataEntity?,

    @SerializedName("rating")
    val rating: String
)