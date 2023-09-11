package com.vd.study.data.local.accounts.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vd.study.data.local.LocalDatabaseCore

@Entity(tableName = LocalDatabaseCore.ACCOUNTS_TABLE_NAME)
data class AccountDataEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "likes_number", defaultValue = "0")
    val likesNumber: Int,

    @ColumnInfo(name = "views_number", defaultValue = "0")
    val viewsNumber: Int
)