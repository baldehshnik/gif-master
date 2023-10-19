package com.vd.study.settings.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vd.study.core.container.Result
import com.vd.study.core.presentation.viewmodel.BaseViewModel
import com.vd.study.settings.R
import com.vd.study.settings.domain.entities.AccountEntity
import com.vd.study.settings.domain.entities.AccountEntityFields
import com.vd.study.settings.domain.exceptions.EmptyFieldException
import com.vd.study.settings.domain.exceptions.IncorrectEmailFormatException
import com.vd.study.settings.domain.exceptions.IncorrectPasswordException
import com.vd.study.settings.domain.exceptions.IncorrectPasswordLengthException
import com.vd.study.settings.domain.usecases.RemoveAccountUseCase
import com.vd.study.settings.domain.usecases.UpdateAccountUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel @AssistedInject constructor(
    private val removeAccountUseCase: RemoveAccountUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    @Assisted("account") private val account: AccountEntity
) : BaseViewModel() {

    private val _fieldErrorMessage = MutableStateFlow<Pair<AccountEntityFields, Int>?>(null)
    val fieldErrorMessage: StateFlow<Pair<AccountEntityFields, Int>?> get() = _fieldErrorMessage

    private val _removeResult = MutableLiveData<Result<Boolean>>()
    val removeResult: LiveData<Result<Boolean>> get() = _removeResult

    private val _updateResult = MutableLiveData<Result<Boolean>>()
    val updateResult: LiveData<Result<Boolean>> get() = _updateResult

    private val _focusFieldLiveEvent = MutableLiveData<AccountEntityFields>()
    val focusFieldLiveEvent: LiveData<AccountEntityFields> get() = _focusFieldLiveEvent

    private val _clearFieldLiveEvent = MutableLiveData<AccountEntityFields>()
    val clearFieldLiveEvent: LiveData<AccountEntityFields> get() = _clearFieldLiveEvent

    fun updateAccount(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                showProgress(_updateResult)

                val result = updateAccountUseCase(
                    account.copy(
                        username = username,
                        email = email,
                        password = password
                    )
                )
                _updateResult.value = result
            } catch (emptyFieldException: EmptyFieldException) {
                handleEmptyFieldException(emptyFieldException)
            } catch (incorrectEmailFormatException: IncorrectEmailFormatException) {
                handleIncorrectEmailFormatException(incorrectEmailFormatException)
            } catch (incorrectPasswordLengthException: IncorrectPasswordLengthException) {
                handleIncorrectPasswordLengthException(incorrectPasswordLengthException)
            }
        }
    }

    fun removeAccount(password: String) {
        viewModelScope.launch {
            try {
                showProgress(_removeResult)

                val result = removeAccountUseCase(account, password)
                _removeResult.value = result
            } catch (incorrectPasswordException: IncorrectPasswordException) {
                _removeResult.value = Result.Error(incorrectPasswordException)
            }
        }
    }

    private fun handleIncorrectPasswordLengthException(incorrectPasswordLengthException: IncorrectPasswordLengthException) {
        focusField(incorrectPasswordLengthException.field)
        setFieldError(incorrectPasswordLengthException.field, R.string.password_too_short)
        clearField(incorrectPasswordLengthException.field)
    }

    private fun handleEmptyFieldException(emptyFieldException: EmptyFieldException) {
        focusField(emptyFieldException.field)
        setFieldError(emptyFieldException.field, R.string.empty_field)
    }

    private fun handleIncorrectEmailFormatException(incorrectEmailFormatException: IncorrectEmailFormatException) {
        focusField(incorrectEmailFormatException.field)
        setFieldError(incorrectEmailFormatException.field, R.string.email_format_error)
    }

    private fun <T> showProgress(liveValue: MutableLiveData<Result<T>>) {
        liveValue.value = Result.Progress
    }

    private fun focusField(accountEntityField: AccountEntityFields) {
        _focusFieldLiveEvent.value = accountEntityField
    }

    private fun clearField(accountEntityField: AccountEntityFields) {
        _clearFieldLiveEvent.value = accountEntityField
    }

    private fun setFieldError(accountEntityField: AccountEntityFields, message: Int) {
        _fieldErrorMessage.value = accountEntityField to message
    }

    @AssistedFactory
    interface Factory {
        fun createViewModel(@Assisted("account") account: AccountEntity): SettingsViewModel
    }

    companion object {

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(factory: Factory, account: AccountEntity): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.createViewModel(account) as T
                }
            }
        }
    }
}