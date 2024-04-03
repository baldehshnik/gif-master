package com.vd.study.gif_master.binding.settings.di

import com.vd.study.gif_master.binding.settings.router.AdapterSettingsRouter
import com.vd.study.settings.presentation.router.SettingsRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface AdapterSettingsRouterModule {

    @Binds
    fun bindAdapterSettingsRouterToSettingsRouter(
        adapterSettingsRouter: AdapterSettingsRouter
    ): SettingsRouter
}
