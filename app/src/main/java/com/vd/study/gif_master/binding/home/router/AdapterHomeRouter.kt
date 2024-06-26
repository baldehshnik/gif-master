package com.vd.study.gif_master.binding.home.router

import com.vd.study.gif_master.MainGraphDirections
import com.vd.study.gif_master.binding.home.mappers.FullGifEntityToViewingGifEntityMapper
import com.vd.study.gif_master.presentation.router.GlobalNavComponentRouter
import com.vd.study.home.domain.entities.FullGifEntity
import com.vd.study.home.presentations.router.HomeRouter
import javax.inject.Inject

class AdapterHomeRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter,
    private val fullGifEntityToViewingGifEntityMapper: FullGifEntityToViewingGifEntityMapper
) : HomeRouter {

    override fun navigateToViewingFragment(gif: FullGifEntity) {
        val action = MainGraphDirections.actionGlobalViewingFragment(
            fullGifEntityToViewingGifEntityMapper.map(gif)
        )
        globalNavComponentRouter.launch(action)
    }

    override fun showBottomBar() {
        globalNavComponentRouter.changeBottomAppBarVisibility(true)
    }
}
