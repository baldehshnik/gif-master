package com.vd.study.sign_in.domain.exceptions

import com.vd.study.sign_in.domain.entities.AccountEntityFields

class IncorrectEmailFormatException : Exception() {
    val field: AccountEntityFields = AccountEntityFields.EMAIL
}