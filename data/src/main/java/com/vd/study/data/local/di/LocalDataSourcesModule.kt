package com.vd.study.data.local.di

import android.content.Context
import androidx.room.Room
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
    fun provideLocalDatabase(
        @ApplicationContext context: Context
    ): LocalDatabase {
        return Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            LocalDatabaseCore.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
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
