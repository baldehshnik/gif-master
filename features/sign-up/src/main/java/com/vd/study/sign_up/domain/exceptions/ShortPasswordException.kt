package com.vd.study.sign_up.domain.exceptions

import com.vd.study.sign_up.domain.entities.AccountRegistrationFields

class ShortPasswordException : Exception() {
    val passwordField = AccountRegistrationFields.PASSWORD
}