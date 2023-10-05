package com.vd.study.sign_up.domain.exceptions

import com.vd.study.sign_up.domain.entities.AccountRegistrationFields

class IncorrectEmailFormatException : Exception() {
    val emailField = AccountRegistrationFields.EMAIL
}