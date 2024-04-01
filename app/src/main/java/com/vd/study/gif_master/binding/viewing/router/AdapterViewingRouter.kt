package com.vd.study.gif_master.binding.viewing.router

import com.vd.study.gif_master.presentation.router.GlobalNavComponentRouter
import com.vd.study.viewing.presentation.router.ViewingRouter
import javax.inject.Inject

class AdapterViewingRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter
) : ViewingRouter {

    override fun popBackStack() {
        globalNavComponentRouter.popBackStack()
    }

    override fun changeBottomBarTheme(default: Boolean) {
        globalNavComponentRouter.changeBottomAppBarTheme(default)
    }
}
