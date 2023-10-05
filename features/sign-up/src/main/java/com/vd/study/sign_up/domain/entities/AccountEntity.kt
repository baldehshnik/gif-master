package com.vd.study.sign_up.domain.entities

const val MIN_PASSWORD_LENGTH = 6

data class AccountEntity(
    val username: String,
    val avatarUrl: String,
    val email: String,
    val password: String
)