package com.vd.study.sign_in.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vd.study.core.container.Result
import com.vd.study.core.presentation.viewmodel.BaseViewModel
import com.vd.study.sign_in.domain.entities.AccountEntity
import com.vd.study.sign_in.domain.entities.AccountEntityFields
import com.vd.study.sign_in.domain.entities.CheckAccountEntity
import com.vd.study.sign_in.domain.exceptions.EmptyFieldException
import com.vd.study.sign_in.domain.exceptions.IncorrectEmailFormatException
import com.vd.study.sign_in.domain.usecases.IsAccountExistsAndCorrectUseCase
import com.vd.study.sign_in.domain.usecases.ReadAccountsUseCase
import com.vd.study.sign_in.presentation.router.SignInRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias AccountsListResult = Result<List<AccountEntity>>

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val readAccountsUseCase: ReadAccountsUseCase,
    private val isAccountExistsAndCorrectUseCase: IsAccountExistsAndCorrectUseCase,
    private val signInRouter: SignInRouter
) : BaseViewModel() {

    private val isOperationInProgress = MutableStateFlow(false)
    private val fieldErrorMessage = MutableStateFlow<Pair<AccountEntityFields, Int>?>(null)

    val stateLiveValue = combine(
        isOperationInProgress,
        fieldErrorMessage,
        ::State
    )

    private val _registeredAccountsLiveEvent = MutableLiveData<AccountsListResult>()
    val registeredAccountsLiveEvent: LiveData<AccountsListResult> = _registeredAccountsLiveEvent

    private val _checkAccountLiveEvent = MutableLiveData<Result<Int>>()
    val checkAccountLiveEvent: LiveData<Result<Int>> get() = _checkAccountLiveEvent

    private val _focusFieldLiveEvent = MutableLiveData<AccountEntityFields>()
    val focusFieldLiveEvent: LiveData<AccountEntityFields> get() = _focusFieldLiveEvent

    private val _clearFieldLiveEvent = MutableLiveData<AccountEntityFields>()
    val clearFieldLiveEvent: LiveData<AccountEntityFields> get() = _clearFieldLiveEvent

    fun singIn(checkAccountEntity: CheckAccountEntity) {
        viewModelScope.launch {
            try {
                showProgress()
                val checkResult = isAccountExistsAndCorrectUseCase(checkAccountEntity)
                _checkAccountLiveEvent.value = checkResult
            } catch (emptyFieldException: EmptyFieldException) {
                handleEmptyFieldException(emptyFieldException)
            } catch (incorrectEmailFormatException: IncorrectEmailFormatException) {
                handleIncorrectEmailFormatException(incorrectEmailFormatException)
            }
        }
    }

    fun hideBottomBar() {
        signInRouter.hideBottomBar()
    }

    fun hideProgress() {
        isOperationInProgress.value = false
    }

    fun navigateToMain() {
        signInRouter.navigateToMain()
    }

    fun navigateToRegistration() {
        signInRouter.navigateToRegistration()
    }

    fun readRegisteredAccounts() {
        viewModelScope.launch {
            showProgress()
            val registeredAccounts = readAccountsUseCase()
            _registeredAccountsLiveEvent.value = registeredAccounts
        }
    }

    private fun handleEmptyFieldException(emptyFieldException: EmptyFieldException) {
        focusField(emptyFieldException.field)
    }

    private fun handleIncorrectEmailFormatException(incorrectEmailFormatException: IncorrectEmailFormatException) {
        focusField(incorrectEmailFormatException.field)
        clearField(incorrectEmailFormatException.field)
    }

    private fun showProgress() {
        isOperationInProgress.value = true
    }

    private fun focusField(field: AccountEntityFields) {
        _focusFieldLiveEvent.value = field
    }

    private fun clearField(field: AccountEntityFields) {
        _clearFieldLiveEvent.value = field
    }

    private fun setFieldError(field: AccountEntityFields, message: Int) {
        fieldErrorMessage.value = field to message
    }

    data class State(
        val isOperationInProgress: Boolean,
        val fieldErrorMessage: Pair<AccountEntityFields, Int>?
    )
}