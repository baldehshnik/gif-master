package com.vd.study.sign_in.domain.exceptions

import com.vd.study.sign_in.domain.entities.AccountEntityFields

class EmptyFieldException(private val _field: AccountEntityFields) : Exception() {
    val field get() = _field
}