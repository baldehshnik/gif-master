package com.vd.study.sign_up.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vd.study.core.container.Result
import com.vd.study.core.presentation.viewmodel.BaseViewModel
import com.vd.study.sign_up.domain.entities.AccountEntity
import com.vd.study.sign_up.domain.usecases.RegisterAccountUseCase
import com.vd.study.sign_up.presentation.router.SignUpRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerAccountUseCase: RegisterAccountUseCase,
    private val signUpRouter: SignUpRouter
) : BaseViewModel() {

    private val _registrationResultLiveValue = MutableLiveData<Result<AccountEntity>>()
    val registrationResultLiveValue: LiveData<Result<AccountEntity>> get() = _registrationResultLiveValue

    fun registerAccount(account: AccountEntity) {
        viewModelScope.launch {
            _registrationResultLiveValue.value = registerAccountUseCase(account).suspendMap {
                account
            }
        }
    }

    fun hideBottomBar() {
        signUpRouter.hideBottomBar()
    }

    fun navigateToMain() {
        signUpRouter.navigateToMain()
    }

    fun returnToSignIn() {
        signUpRouter.popUpToSignIn()
    }
}
