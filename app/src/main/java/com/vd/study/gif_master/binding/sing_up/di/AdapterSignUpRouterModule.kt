package com.vd.study.gif_master.binding.sing_up.di

import com.vd.study.gif_master.binding.sing_up.router.AdapterSignUpRouter
import com.vd.study.sign_up.presentation.router.SignUpRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface AdapterSignUpRouterModule {

    @Binds
    fun bindAdapterSignUpRouterToSignUpRouter(adapterSignUpRouter: AdapterSignUpRouter): SignUpRouter
}
