package com.vd.study.sign_up.domain.entities

import java.util.Date

data class AccountEntity(
    val username: String,
    val avatarUrl: String,
    val email: String,
    val password: String,
    val date: Date
)
