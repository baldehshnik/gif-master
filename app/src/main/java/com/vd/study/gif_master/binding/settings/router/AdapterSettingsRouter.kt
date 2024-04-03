package com.vd.study.gif_master.binding.settings.router

import com.vd.study.gif_master.R
import com.vd.study.gif_master.presentation.router.GlobalNavComponentRouter
import com.vd.study.settings.presentation.router.SettingsRouter
import javax.inject.Inject

class AdapterSettingsRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter
) : SettingsRouter {

    override fun exitFromAccount() {
        globalNavComponentRouter.popToInclusive(R.id.homeFragment)
        globalNavComponentRouter.launch(R.id.signInFragment)
    }

    override fun popBackStack() {
        globalNavComponentRouter.popBackStack()
        globalNavComponentRouter.changeBottomAppBarVisibility(true)
    }
}
