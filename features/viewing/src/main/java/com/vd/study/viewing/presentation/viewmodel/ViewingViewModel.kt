package com.vd.study.viewing.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.vd.study.viewing.domain.entities.GifEntity
import com.vd.study.core.dispatchers.Dispatchers
import com.vd.study.core.presentation.viewmodel.BaseViewModel
import com.vd.study.viewing.domain.usecases.UpdateGifInLocalDatabaseUseCase
import com.vd.study.viewing.presentation.router.ViewingRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewingViewModel @Inject constructor(
    private val dispatchers: Dispatchers,
    private val updateGifInLocalDatabaseUseCase: UpdateGifInLocalDatabaseUseCase,
    private val router: ViewingRouter
) : BaseViewModel() {

    fun popBackStack() {
        router.popBackStack()
    }

    fun updateGifByLike(gifEntity: GifEntity) {
        viewModelScope.launch(dispatchers.defaultDispatcher) {
            val likedGif = gifEntity.copy(isLiked = !gifEntity.isLiked)
            updateGifInLocalDatabaseUseCase(likedGif)
        }
    }

    fun updateGifBySave(gifEntity: GifEntity) {
        viewModelScope.launch(dispatchers.defaultDispatcher) {
            val savedGif = gifEntity.copy(isSaved = !gifEntity.isSaved)
            updateGifInLocalDatabaseUseCase(savedGif)
        }
    }
}