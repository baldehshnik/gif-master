package com.vd.study.core.global

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountIdentifier @Inject constructor() {
    var accountIdentifier: Int = -1
}