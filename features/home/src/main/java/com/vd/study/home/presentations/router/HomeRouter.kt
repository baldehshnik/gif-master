package com.vd.study.home.presentations.router

import com.vd.study.home.domain.entities.FullGifEntity

interface HomeRouter {

    fun navigateToViewingFragment(gif: FullGifEntity)

}