package com.vd.study.sign_in.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vd.study.core.container.Result
import com.vd.study.core.presentation.viewmodel.BaseViewModel
import com.vd.study.sign_in.domain.entities.AccountEntity
import com.vd.study.sign_in.domain.entities.CheckAccountEntity
import com.vd.study.sign_in.domain.usecases.IsAccountExistsAndCorrectUseCase
import com.vd.study.sign_in.domain.usecases.ReadAccountsUseCase
import com.vd.study.sign_in.presentation.router.SignInRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias AccountsListResult = Result<List<AccountEntity>>

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val readAccountsUseCase: ReadAccountsUseCase,
    private val isAccountExistsAndCorrectUseCase: IsAccountExistsAndCorrectUseCase,
    private val signInRouter: SignInRouter
) : BaseViewModel() {

    private val _registeredAccountsLiveEvent = MutableLiveData<AccountsListResult>()
    val registeredAccountsLiveEvent: LiveData<AccountsListResult> = _registeredAccountsLiveEvent

    private val _checkAccountLiveEvent = MutableLiveData<Result<Int>>()
    val checkAccountLiveEvent: LiveData<Result<Int>> get() = _checkAccountLiveEvent

    fun singIn(checkAccountEntity: CheckAccountEntity) {
        viewModelScope.launch {
            val checkResult = isAccountExistsAndCorrectUseCase(checkAccountEntity)
            _checkAccountLiveEvent.value = checkResult
        }
    }

    fun hideBottomBar() {
        signInRouter.hideBottomBar()
    }

    fun navigateToMain() {
        signInRouter.navigateToMain()
    }

    fun navigateToRegistration() {
        signInRouter.navigateToRegistration()
    }

    fun readRegisteredAccounts() {
        viewModelScope.launch {
            val registeredAccounts = readAccountsUseCase()
            _registeredAccountsLiveEvent.value = registeredAccounts
        }
    }
}
