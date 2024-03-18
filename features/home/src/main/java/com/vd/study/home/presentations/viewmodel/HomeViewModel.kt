package com.vd.study.home.presentations.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.vd.study.core.container.Result
import com.vd.study.core.dispatchers.Dispatchers
import com.vd.study.core.presentation.viewmodel.BaseViewModel
import com.vd.study.home.domain.entities.FullGifEntity
import com.vd.study.home.domain.entities.GifEntity
import com.vd.study.home.domain.entities.LikeAndSaveStatusEntity
import com.vd.study.home.domain.usecases.PagingReadGifsUseCase
import com.vd.study.home.domain.usecases.ReadLikeAndSaveStatusUseCase
import com.vd.study.home.domain.usecases.UpdateGifUseCase
import com.vd.study.home.presentations.router.HomeRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatchers: Dispatchers,
    private val pagingReadGifsUseCase: PagingReadGifsUseCase,
    private val readLikeAndSaveStatusUseCase: ReadLikeAndSaveStatusUseCase,
    private val updateGifUseCase: UpdateGifUseCase,
    private val router: HomeRouter
) : BaseViewModel() {

    private val _readGifsResult = MutableLiveData<PagingData<FullGifEntity>>()
    val readGifsResult: LiveData<PagingData<FullGifEntity>> get() = _readGifsResult

    private val _updateGifResult = MutableLiveData<FullGifEntity?>()
    val updateGifResult: LiveData<FullGifEntity?> get() = _updateGifResult

    fun updateGif(newGif: FullGifEntity) {
        viewModelScope.launch {
            val result = updateGifUseCase(newGif)
            val value = result.getOrNull() ?: return@launch

            withContext(dispatchers.mainDispatcher) {
                if (value) {
                    _updateGifResult.value = newGif
                } else {
                    _updateGifResult.value = null
                }
            }
        }
    }

    fun navigateToViewingFragment(gif: FullGifEntity) {
        router.navigateToViewingFragment(gif)
    }

    fun showBottomBar() {
        router.showBottomBar()
    }

    private fun readGifs() {
        viewModelScope.launch {
            val result = pagingReadGifsUseCase().cachedIn(viewModelScope)
            handleGifsSynchronization(result)
        }
    }

    private suspend fun handleGifsSynchronization(
        dataFlow: Flow<PagingData<GifEntity>>
    ) = withContext(dispatchers.defaultDispatcher) {
        dataFlow.collect { data ->
            val mappedData = data.map { gifEntity ->
                val likeAndSaveStatusEntity = readLikeAndSaveStatusUseCase(gifEntity)
                val status = handleLikeAndSaveStatusReading(likeAndSaveStatusEntity)
                FullGifEntity.getInstateWith(gifEntity, status)
            }

            withContext(dispatchers.mainDispatcher) {
                _readGifsResult.value = mappedData
            }
        }
    }

    private fun handleLikeAndSaveStatusReading(
        status: Result<LikeAndSaveStatusEntity>
    ): LikeAndSaveStatusEntity {
        val emptyStatus = LikeAndSaveStatusEntity(gifId = -1, isLiked = false, isSaved = false, isViewed = false)
        return status.getOrNull() ?: emptyStatus
    }

    init {
        readGifs()
    }
}