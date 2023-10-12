package com.vd.study.settings.domain.exceptions

import com.vd.study.settings.domain.entities.AccountEntityFields

class EmptyFieldException(private val _field: AccountEntityFields) : Exception() {
    val field get() = _field
}