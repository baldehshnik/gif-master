package com.vd.study.data.local.gifs.entities

import androidx.room.ColumnInfo

data class LocalGifAuthorDataEntity(

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String
)