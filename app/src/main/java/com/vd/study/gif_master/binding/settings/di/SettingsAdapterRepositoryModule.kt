package com.vd.study.gif_master.binding.settings.di

import com.vd.study.gif_master.binding.settings.repositories.SettingsAdapterRepository
import com.vd.study.settings.domain.repositories.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface SettingsAdapterRepositoryModule {

    @Binds
    fun bindSettingsAdapterRepositoryToSettingsRepository(
        settingsAdapterRepository: SettingsAdapterRepository
    ): SettingsRepository
}