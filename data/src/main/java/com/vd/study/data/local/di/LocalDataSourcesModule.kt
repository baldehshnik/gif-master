package com.vd.study.data.local.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vd.study.data.local.LocalDatabase
import com.vd.study.data.local.LocalDatabaseCore
import com.vd.study.data.local.accounts.sources.AccountDao
import com.vd.study.data.local.gifs.sources.LocalGifsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalDataSourcesModule {

    @Singleton
    @Provides
    fun provideLocalDatabaseCallback(): RoomDatabase.Callback {
        return object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL(
                    """
                        CREATE TRIGGER ${LocalDatabaseCore.GIF_UPDATED_TRIGGER_NAME} AFTER UPDATE 
                        ON ${LocalDatabaseCore.GIFS_TABLE_NAME}
                        BEGIN
                        DELETE FROM ${LocalDatabaseCore.GIFS_TABLE_NAME}
                        WHERE is_saved = 0 AND is_liked = 0;
                        END;
                    """.trimIndent()
                )
            }
        }
    }

    @Singleton
    @Provides
    fun provideLocalDatabase(
        @ApplicationContext context: Context,
        callback: RoomDatabase.Callback
    ): LocalDatabase {
        return Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            LocalDatabaseCore.DATABASE_NAME
        ).addCallback(callback).build()
    }

    @Provides
    fun provideLocalGifsDao(localDatabase: LocalDatabase): LocalGifsDao {
        return localDatabase.gifsDao
    }

    @Provides
    fun provideAccountDao(localDatabase: LocalDatabase): AccountDao {
        return localDatabase.accountDao
    }
}