package com.vd.study.data.local.accounts.entities

import androidx.room.ColumnInfo

data class CheckAccountDataEntity(

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "password")
    val password: String
)