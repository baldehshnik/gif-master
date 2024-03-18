package com.vd.study.sign_up.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vd.study.core.container.Result
import com.vd.study.core.presentation.viewmodel.BaseViewModel
import com.vd.study.sign_up.R
import com.vd.study.sign_up.domain.entities.AccountEntity
import com.vd.study.sign_up.domain.entities.AccountRegistrationFields
import com.vd.study.sign_up.domain.exceptions.EmptyFieldException
import com.vd.study.sign_up.domain.exceptions.IncorrectEmailFormatException
import com.vd.study.sign_up.domain.exceptions.ShortPasswordException
import com.vd.study.sign_up.domain.usecases.RegisterAccountUseCase
import com.vd.study.sign_up.presentation.router.SignUpRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerAccountUseCase: RegisterAccountUseCase,
    private val signUpRouter: SignUpRouter
) : BaseViewModel() {

    private val signUpInProgressFlow = MutableStateFlow(false)
    private val fieldErrorMessageFlow = MutableStateFlow<Pair<AccountRegistrationFields, Int>?>(null)

    private val _registrationResultLiveValue = MutableLiveData<Result<AccountEntity>>()
    val registrationResultLiveValue: LiveData<Result<AccountEntity>> get() = _registrationResultLiveValue

    private val _focusFieldLiveEvent = MutableLiveData<AccountRegistrationFields>()
    val focusFieldLiveEvent: LiveData<AccountRegistrationFields> get() = _focusFieldLiveEvent

    private val _clearFieldLiveEvent = MutableLiveData<AccountRegistrationFields>()
    val clearFieldLiveEvent: LiveData<AccountRegistrationFields> get() = _clearFieldLiveEvent

    val stateLiveValue = combine(
        signUpInProgressFlow,
        fieldErrorMessageFlow,
        ::State
    )

    fun registerAccount(account: AccountEntity) {
        viewModelScope.launch {
            try {
                showProgress()
                _registrationResultLiveValue.value = registerAccountUseCase(account).suspendMap {
                    account
                }
            } catch (emptyFieldException: EmptyFieldException) {
                handleEmptyFieldException(emptyFieldException)
            } catch (incorrectEmailFormatException: IncorrectEmailFormatException) {
                handleIncorrectEmailFormatException(incorrectEmailFormatException)
            } catch (shortPasswordException: ShortPasswordException) {
                handleShortPasswordException(shortPasswordException)
            }
        }
    }

    fun hideBottomBar() {
        signUpRouter.hideBottomBar()
    }

    fun hideProgress() {
        signUpInProgressFlow.value = false
    }

    fun navigateToMain() {
        signUpRouter.navigateToMain()
    }

    fun returnToSignIn() {
        signUpRouter.popUpToSignIn()
    }

    private fun showProgress() {
        signUpInProgressFlow.value = true
    }

    private fun handleEmptyFieldException(emptyFieldException: EmptyFieldException) {
        focusField(emptyFieldException.accountRegistrationField)
        setFieldError(emptyFieldException.accountRegistrationField, R.string.sign_up_empty_field)
    }

    private fun handleIncorrectEmailFormatException(incorrectEmailFormatException: IncorrectEmailFormatException) {
        focusField(incorrectEmailFormatException.emailField)
        setFieldError(incorrectEmailFormatException.emailField, R.string.sign_up_incorrect_email)
    }

    private fun handleShortPasswordException(shortPasswordException: ShortPasswordException) {
        focusField(shortPasswordException.passwordField)
        clearField(shortPasswordException.passwordField)
        setFieldError(shortPasswordException.passwordField, R.string.sign_up_short_password)
    }

    private fun focusField(field: AccountRegistrationFields) {
        _focusFieldLiveEvent.value = field
    }

    private fun clearField(field: AccountRegistrationFields) {
        _clearFieldLiveEvent.value = field
    }

    private fun setFieldError(field: AccountRegistrationFields, message: Int) {
        fieldErrorMessageFlow.value = field to message
    }

    data class State(
        val signUpInProgress: Boolean,
        val fieldErrorMessage: Pair<AccountRegistrationFields, Int>?
    )
}