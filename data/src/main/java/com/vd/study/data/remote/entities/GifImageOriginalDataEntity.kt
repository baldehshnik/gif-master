package com.vd.study.data.remote.entities

import com.google.gson.annotations.SerializedName

data class GifImageOriginalDataEntity(
    @SerializedName("url")
    val url: String,

    @SerializedName("height")
    val height: Int,

    @SerializedName("width")
    val width: Int
)
