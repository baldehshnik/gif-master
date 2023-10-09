package com.vd.study.account.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vd.study.account.domain.entities.AccountEntity
import com.vd.study.account.domain.entities.GifEntity
import com.vd.study.account.domain.exceptions.NothingFoundException
import com.vd.study.account.domain.usecase.ReadLikedGifsCountUseCase
import com.vd.study.account.domain.usecase.ReadLikedGifsUseCase
import com.vd.study.account.domain.usecase.ReadSavedAccountUseCase
import com.vd.study.core.container.Result
import com.vd.study.core.viewmodel.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class AccountViewModel @AssistedInject constructor(
    private val readLikedGifsCountUseCase: ReadLikedGifsCountUseCase,
    private val readLikedGifsUseCase: ReadLikedGifsUseCase,
    private val readSavedAccountUseCase: ReadSavedAccountUseCase,
    @Assisted("email") private val email: String
) : BaseViewModel() {

    init {
        readAccount()
    }

    private var accountId = -1

    private val _accountLiveValue = MutableLiveData<Result<AccountEntity>>()
    val accountLiveValue: LiveData<Result<AccountEntity>> get() = _accountLiveValue

    private val _likedGifsCountLiveValue = MutableLiveData<Result<Int>>()
    val likedGifsCountLiveData: LiveData<Result<Int>> get() = _likedGifsCountLiveValue

    private val _likedGifsLiveValue = MutableLiveData<Result<List<GifEntity>>>()
    val likedGifsLiveData: LiveData<Result<List<GifEntity>>> get() = _likedGifsLiveValue

    fun readLikedGifsCount() {
        viewModelScope.launch {
            showProgress(_likedGifsCountLiveValue)

            val countResult = readLikedGifsCountUseCase(accountId)
            _likedGifsCountLiveValue.value = countResult
        }
    }

    fun readLikedGifs() {
        viewModelScope.launch {
            showProgress(_likedGifsLiveValue)

            val gifsResult = readLikedGifsUseCase(accountId)
            handleLikedGifsReadingResult(gifsResult)
        }
    }

    private fun readAccount() {
        viewModelScope.launch {
            showProgress(_accountLiveValue)

            val accountResult = readSavedAccountUseCase(email)
            accountId = accountResult.getOrNull()?.id!!
            _accountLiveValue.value = accountResult
        }
    }

    private suspend fun handleLikedGifsReadingResult(answer: Result<Flow<List<GifEntity>>>) {
        when (answer) {
            Result.Progress -> {
                _likedGifsLiveValue.value = Result.Progress
            }
            is Result.Error -> {
                _likedGifsLiveValue.value = answer.map { it }
            }

            is Result.Correct -> {
                val value = answer.getOrNull()
                if (value != null) {
                    value.collect {
                        _likedGifsLiveValue.value = Result.Correct(it)
                    }
                } else {
                    _likedGifsLiveValue.value = Result.Error(NothingFoundException())
                }
            }
        }
    }

    private fun <T> showProgress(liveValue: MutableLiveData<Result<T>>) {
        liveValue.value = Result.Progress
    }

    @AssistedFactory
    interface Factory {
        fun createViewModel(@Assisted("email") email: String): AccountViewModel
    }

    companion object {

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(factory: Factory, email: String): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.createViewModel(email) as T
                }
            }
        }
    }
}