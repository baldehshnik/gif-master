package com.vd.study.data.remote.entities

import com.google.gson.annotations.SerializedName

data class GifAuthorDataEntity(

    @SerializedName("display_name")
    val username: String,

    @SerializedName("avatar_url")
    val avatarUrl: String
)