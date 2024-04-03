package com.vd.study.settings.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vd.study.core.container.Result
import com.vd.study.core.dispatchers.Dispatchers
import com.vd.study.core.presentation.viewmodel.BaseViewModel
import com.vd.study.settings.domain.entities.AccountEntity
import com.vd.study.settings.domain.usecases.ReadAccountUseCase
import com.vd.study.settings.domain.usecases.ReadLikedGifsCountUseCase
import com.vd.study.settings.domain.usecases.ReadSavedGifsCountUseCase
import com.vd.study.settings.domain.usecases.RemoveAccountUseCase
import com.vd.study.settings.domain.usecases.UpdateAccountUseCase
import com.vd.study.settings.presentation.router.SettingsRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val router: SettingsRouter,
    private val dispatchers: Dispatchers,
    private val removeAccountUseCase: RemoveAccountUseCase,
    private val readAccountUseCase: ReadAccountUseCase,
    private val readLikedGifsCountUseCase: ReadLikedGifsCountUseCase,
    private val readSavedGifsCountUseCase: ReadSavedGifsCountUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase
) : BaseViewModel() {

    private val _deleteAccountResult = MutableLiveData<Result<Boolean>>()
    val deleteAccountsResult: LiveData<Result<Boolean>> get() = _deleteAccountResult

    private val _readAccountResult = MutableLiveData<Result<AccountEntity>>()
    val readAccountResult: LiveData<Result<AccountEntity>> get() = _readAccountResult

    private val _readLikedGifsCountResult = MutableLiveData<Result<Int>>()
    val readLikedGifsCountResult: LiveData<Result<Int>> get() = _readLikedGifsCountResult

    private val _readSavedGifsCountResult = MutableLiveData<Result<Int>>()
    val readSavedGifsCountResult: LiveData<Result<Int>> get() = _readSavedGifsCountResult

    private val _updateAccountResult = MutableLiveData<Result<Boolean>>()
    val updateAccountResult: LiveData<Result<Boolean>> get() = _updateAccountResult

    fun removeAccount() {
        viewModelScope.launch(dispatchers.defaultDispatcher) {
            val result = removeAccountUseCase()

            withContext(dispatchers.mainDispatcher) {
                _deleteAccountResult.value = result
            }
        }
    }

    fun updateAccount(account: AccountEntity) {
        viewModelScope.launch(dispatchers.defaultDispatcher) {
            val result = updateAccountUseCase(account)

            withContext(dispatchers.mainDispatcher) {
                _updateAccountResult.value = result
            }
        }
    }

    fun exitFromAccount() {
        router.exitFromAccount()
    }

    fun returnBack() {
        router.popBackStack()
    }

    private fun readAccount() {
        viewModelScope.launch(dispatchers.defaultDispatcher) {
            val result = readAccountUseCase()

            withContext(dispatchers.mainDispatcher) {
                _readAccountResult.value = result
            }
        }
    }

    private fun readLikedGifsCount() {
        viewModelScope.launch(dispatchers.defaultDispatcher) {
            val result = readLikedGifsCountUseCase()

            withContext(dispatchers.mainDispatcher) {
                _readLikedGifsCountResult.value = result
            }
        }
    }

    private fun readSavedGifsCount() {
        viewModelScope.launch(dispatchers.defaultDispatcher) {
            val result = readSavedGifsCountUseCase()

            withContext(dispatchers.mainDispatcher) {
                _readSavedGifsCountResult.value = result
            }
        }
    }

    init {
        readAccount()
        readLikedGifsCount()
        readSavedGifsCount()
    }
}
