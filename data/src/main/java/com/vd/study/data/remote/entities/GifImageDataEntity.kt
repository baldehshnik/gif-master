package com.vd.study.data.remote.entities

import com.google.gson.annotations.SerializedName

data class GifImageDataEntity(
    @SerializedName("original")
    val original: GifImageOriginalDataEntity
)