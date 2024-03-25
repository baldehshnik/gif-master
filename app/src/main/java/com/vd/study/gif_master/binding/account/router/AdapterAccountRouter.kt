package com.vd.study.gif_master.binding.account.router

import com.vd.study.account.domain.entities.GifEntity
import com.vd.study.account.presentation.router.AccountRouter
import com.vd.study.gif_master.MainGraphDirections
import com.vd.study.gif_master.binding.account.mappers.GifEntityToViewingGifEntityMapper
import com.vd.study.gif_master.presentation.router.GlobalNavComponentRouter
import javax.inject.Inject

class AdapterAccountRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter,
    private val gifEntityToViewingGifEntityMapper: GifEntityToViewingGifEntityMapper
) : AccountRouter {

    override fun navigateToViewingFragment(gif: GifEntity) {
        globalNavComponentRouter.launch(
            MainGraphDirections.actionGlobalViewingFragment(gifEntityToViewingGifEntityMapper.map(gif))
        )
    }
}