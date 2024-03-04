package com.vd.study.gif_master.binding.viewing.di

import com.vd.study.gif_master.binding.viewing.router.AdapterViewingRouter
import com.vd.study.viewing.presentation.router.ViewingRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface AdapterViewingRouterModule {

    @Binds
    fun bindAdapterViewingRouterToViewingRouter(
        adapterViewingRouter: AdapterViewingRouter
    ) : ViewingRouter
}