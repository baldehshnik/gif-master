package com.vd.study.sign_up.domain.exceptions

import com.vd.study.sign_up.domain.entities.AccountRegistrationFields

class EmptyFieldException(
    private val _accountRegistrationField: AccountRegistrationFields
) : Exception() {
    val accountRegistrationField get() = _accountRegistrationField
}