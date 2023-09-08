package com.vd.study.data.remote.di

import com.vd.study.data.RemoteGifsDataRepository
import com.vd.study.data.remote.RemoteGifsDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RemoteRepositoriesModule {

    @Binds
    fun bindRemoteGifsDataRepositoryImplToRemoteGifsDataRepository(
        repository: RemoteGifsDataRepositoryImpl
    ): RemoteGifsDataRepository
}