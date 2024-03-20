package com.vd.study.data.local.accounts.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.vd.study.data.local.LocalDatabaseCore
import java.util.Date

@Entity(
    tableName = LocalDatabaseCore.ACCOUNTS_TABLE_NAME,
    indices = [Index(value = ["email"], unique = true)]
)
data class AccountDataEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "date")
    val date: Date
)
