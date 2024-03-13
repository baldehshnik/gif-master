package com.vd.study.gif_master.binding.search.router

import com.vd.study.gif_master.presentation.router.GlobalNavComponentRouter
import com.vd.study.search.presentation.router.SearchRouter
import javax.inject.Inject

class AdapterSearchRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter
) : SearchRouter {

    override fun popBackStack() {
        globalNavComponentRouter.changeBottomAppBarVisibility(true)
        globalNavComponentRouter.popBackStack()
    }
}