package com.vd.study.gif_master.binding.search.di

import com.vd.study.gif_master.binding.search.router.AdapterSearchRouter
import com.vd.study.search.presentation.router.SearchRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface AdapterSearchRouterModule {

    @Binds
    fun bindAdapterSearchRouterToSearchRouter(
        adapterSearchRouter: AdapterSearchRouter
    ): SearchRouter
}