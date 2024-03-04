package com.vd.study.gif_master.binding.home.di

import com.vd.study.gif_master.binding.home.router.AdapterHomeRouter
import com.vd.study.home.presentations.router.HomeRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface AdapterHomeRouterModule {

    @Binds
    fun bindAdapterHomeRouterToHomeRouter(
        adapterHomeRouter: AdapterHomeRouter
    ) : HomeRouter
}