package com.vd.study.home.presentations.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.vd.study.core.container.Result
import com.vd.study.core.dispatchers.Dispatchers
import com.vd.study.core.viewmodel.BaseViewModel
import com.vd.study.home.domain.entities.FullGifEntity
import com.vd.study.home.domain.entities.GifEntity
import com.vd.study.home.domain.entities.LikeAndSaveStatusEntity
import com.vd.study.home.domain.usecases.PagingReadGifsUseCase
import com.vd.study.home.domain.usecases.ReadLikeAndSaveStatusUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class HomeViewModel @AssistedInject constructor(
    private val dispatchers: Dispatchers,
    private val pagingReadGifsUseCase: PagingReadGifsUseCase,
    private val readLikeAndSaveStatusUseCase: ReadLikeAndSaveStatusUseCase,
    @Assisted("account") private val accountId: Int
) : BaseViewModel() {

    private val _readGifsResult = MutableLiveData<PagingData<FullGifEntity>>()
    val readGifsResult: LiveData<PagingData<FullGifEntity>> get() = _readGifsResult

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
                val likeAndSaveStatusEntity = readLikeAndSaveStatusUseCase(accountId, gifEntity)
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
        val emptyStatus = LikeAndSaveStatusEntity(gifId = -1, isLiked = false, isSaved = false)
        return status.getOrNull() ?: emptyStatus
    }

    init {
        readGifs()
    }

    @AssistedFactory
    interface Factory {
        fun createViewModel(@Assisted("account") accountId: Int): HomeViewModel
    }

    companion object {

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(factory: Factory, accountId: Int): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.createViewModel(accountId) as T
                }
            }
        }
    }
}