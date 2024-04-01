package com.vd.study.data.local.gifs.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.vd.study.data.local.LocalDatabaseCore
import com.vd.study.data.local.accounts.entities.AccountDataEntity

@Entity(
    tableName = LocalDatabaseCore.GIFS_TABLE_NAME,
    foreignKeys = [ForeignKey(
        AccountDataEntity::class,
        parentColumns = ["id"],
        childColumns = ["account_id"],
        onDelete = CASCADE,
        onUpdate = CASCADE
    )],
    indices = [Index(value = ["source_url"], unique = true)]
)
data class LocalGifDataEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "source_url")
    val url: String,

    @ColumnInfo(name = "rating")
    val rating: String,

    @Embedded(prefix = "author_")
    val author: LocalGifAuthorDataEntity? = null,

    @ColumnInfo(name = "is_liked")
    val isLiked: Boolean,

    @ColumnInfo(name = "is_saved")
    val isSaved: Boolean,

    @ColumnInfo(name = "is_viewed")
    val isViewed: Boolean,

    @ColumnInfo(name = "account_id", index = true)
    val accountId: Int
)
