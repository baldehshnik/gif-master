package com.vd.study.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vd.study.data.local.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
object TestLocalDataSourcesModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryRoomDatabase(
        @ApplicationContext context: Context,
        callback: RoomDatabase.Callback
    ): LocalDatabase {
        return Room.inMemoryDatabaseBuilder(context, LocalDatabase::class.java)
            .addCallback(callback)
            .allowMainThreadQueries()
            .build()
    }
}