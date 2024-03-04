package com.vd.study.viewing.presentation.viewmodel

import com.vd.study.viewing.domain.entities.GifEntity
import com.vd.study.core.dispatchers.Dispatchers
import com.vd.study.core.presentation.viewmodel.BaseViewModel
import com.vd.study.viewing.presentation.router.ViewingRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewingViewModel @Inject constructor(
    private val dispatchers: Dispatchers,
    private val router: ViewingRouter
) : BaseViewModel() {

    fun popBackStack() {
        router.popBackStack()
    }

    fun updateGifByLike(gifEntity: GifEntity) {

    }

    fun updateGifBySave(gifEntity: GifEntity) {

    }

}