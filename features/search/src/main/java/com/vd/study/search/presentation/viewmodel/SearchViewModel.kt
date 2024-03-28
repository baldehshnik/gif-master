package com.vd.study.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.vd.study.core.container.Result
import com.vd.study.core.dispatchers.Dispatchers
import com.vd.study.core.presentation.viewmodel.BaseViewModel
import com.vd.study.search.domain.entities.GifEntity
import com.vd.study.search.domain.entities.GifStatusEntity
import com.vd.study.search.domain.entities.NetworkGifEntity
import com.vd.study.search.domain.usecases.PagingReadSearchGifsUseCase
import com.vd.study.search.domain.usecases.ReadGifStatusUseCase
import com.vd.study.search.presentation.router.SearchRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val pagingReadSearchGifsUseCase: PagingReadSearchGifsUseCase,
    private val readGifStatusUseCase: ReadGifStatusUseCase,
    private val dispatchers: Dispatchers,
    private val router: SearchRouter
) : BaseViewModel() {

    private val _gifsReadingResult = MutableLiveData<PagingData<GifEntity>>()
    val gifsReadingResult: LiveData<PagingData<GifEntity>> get() = _gifsReadingResult

    fun navigateToViewingFragment(gif: GifEntity) {
        router.navigateToViewingFragment(gif)
    }

    fun popBackStack() {
        router.popBackStack()
    }

    fun readSearchGifs(query: String) {
        viewModelScope.launch {
            val result = pagingReadSearchGifsUseCase(query).cachedIn(viewModelScope)
            handleGifsReading(result)
        }
    }

    private suspend fun handleGifsReading(flow: Flow<PagingData<NetworkGifEntity>>) =
        withContext(dispatchers.defaultDispatcher) {
            flow.collect { data ->
                val mappedData = data.map {
                    val readGifStatus = readGifStatusUseCase(it)
                    val realStatus = getGifStatus(readGifStatus)
                    GifEntity.getEntity(it, realStatus)
                }

                withContext(dispatchers.mainDispatcher) {
                    _gifsReadingResult.value = mappedData
                }
            }
        }

    private fun getGifStatus(statusEntity: Result<GifStatusEntity>): GifStatusEntity {
        return statusEntity.getOrNull() ?: GifStatusEntity.getEmpty()
    }
}
