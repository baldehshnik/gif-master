package com.vd.study.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vd.study.data.local.accounts.entities.AccountDataEntity
import com.vd.study.data.local.accounts.sources.AccountDao
import com.vd.study.data.local.accounts.sources.DateConverter
import com.vd.study.data.local.gifs.entities.LocalGifDataEntity
import com.vd.study.data.local.gifs.sources.LocalGifsDao

@Database(
    entities = [AccountDataEntity::class, LocalGifDataEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract val accountDao: AccountDao
    abstract val gifsDao: LocalGifsDao
}
