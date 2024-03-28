package com.vd.study.gif_master.binding.search.router

import com.vd.study.gif_master.MainGraphDirections
import com.vd.study.gif_master.binding.search.mappers.SearchGifEntityToViewingGifEntityMapper
import com.vd.study.gif_master.presentation.router.GlobalNavComponentRouter
import com.vd.study.search.domain.entities.GifEntity
import com.vd.study.search.presentation.router.SearchRouter
import javax.inject.Inject

class AdapterSearchRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter,
    private val searchGifEntityToViewingGifEntityMapper: SearchGifEntityToViewingGifEntityMapper
) : SearchRouter {

    override fun navigateToViewingFragment(gif: GifEntity) {
        globalNavComponentRouter.launch(
            MainGraphDirections.actionGlobalViewingFragment(
                searchGifEntityToViewingGifEntityMapper.map(gif)
            )
        )
    }

    override fun popBackStack() {
        globalNavComponentRouter.changeBottomAppBarVisibility(true)
        globalNavComponentRouter.popBackStack()
    }
}
