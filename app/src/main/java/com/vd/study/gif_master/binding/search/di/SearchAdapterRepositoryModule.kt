package com.vd.study.gif_master.binding.search.di

import com.vd.study.gif_master.binding.search.repositories.SearchAdapterRepository
import com.vd.study.search.domain.repositories.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface SearchAdapterRepositoryModule {

    @Binds
    fun bindSearchAdapterRepositoryToSearchRepository(
        searchAdapterRepository: SearchAdapterRepository
    ): SearchRepository
}