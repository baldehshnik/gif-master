package com.vd.study.gif_master.binding.home.di

import com.vd.study.gif_master.binding.home.repositories.HomeAdapterRepository
import com.vd.study.home.domain.repositories.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface HomeAdapterRepositoryModule {

    @Binds
    fun bindHomeAdapterRepositoryToHomeRepository(
        homeAdapterRepository: HomeAdapterRepository
    ): HomeRepository
}