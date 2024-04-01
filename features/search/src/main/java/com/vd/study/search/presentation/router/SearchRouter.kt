package com.vd.study.search.presentation.router

import com.vd.study.search.domain.entities.GifEntity

interface SearchRouter {

    fun navigateToViewingFragment(gif: GifEntity)

    fun popBackStack()

}
