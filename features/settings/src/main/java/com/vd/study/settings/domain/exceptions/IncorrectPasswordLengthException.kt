package com.vd.study.settings.domain.exceptions

import com.vd.study.settings.domain.entities.AccountEntityFields

class IncorrectPasswordLengthException : Exception() {
    val field: AccountEntityFields get() = AccountEntityFields.PASSWORD
}