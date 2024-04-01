package com.vd.study.gif_master.binding.viewing.di

import com.vd.study.viewing.domain.repositories.ViewingRepository
import com.vd.study.gif_master.binding.viewing.repositories.ViewingAdapterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface ViewingAdapterRepositoryModule {

    @Binds
    fun bindViewingAdapterRepositoryToViewingRepository(
        viewingAdapterRepository: ViewingAdapterRepository
    ) : ViewingRepository
}
