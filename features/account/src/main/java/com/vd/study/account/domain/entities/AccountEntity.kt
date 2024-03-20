package com.vd.study.account.domain.entities

import java.util.Date

data class AccountEntity(
    val id: Int,
    val username: String,
    val avatarUrl: String,
    val date: Date
)