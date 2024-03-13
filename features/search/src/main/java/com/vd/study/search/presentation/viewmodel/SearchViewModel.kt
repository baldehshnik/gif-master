package com.vd.study.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vd.study.core.dispatchers.Dispatchers
import com.vd.study.core.presentation.viewmodel.BaseViewModel
import com.vd.study.search.domain.entities.NetworkGifEntity
import com.vd.study.search.domain.usecases.PagingReadSearchGifsUseCase
import com.vd.study.search.presentation.router.SearchRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val pagingReadSearchGifsUseCase: PagingReadSearchGifsUseCase,
    private val dispatchers: Dispatchers,
    private val router: SearchRouter
) : BaseViewModel() {

    private val _gifsReadingResult = MutableLiveData<PagingData<NetworkGifEntity>>()
    val gifsReadingResult: LiveData<PagingData<NetworkGifEntity>> get() = _gifsReadingResult

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
                // add connection with local database
                withContext(dispatchers.mainDispatcher) {
                    _gifsReadingResult.value = data
                }
            }
        }
}
