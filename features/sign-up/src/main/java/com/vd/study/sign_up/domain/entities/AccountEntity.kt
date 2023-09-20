package com.vd.study.sign_up.domain.entities

data class AccountEntity(
    val username: String,
    val avatarUrl: String,
    val email: String,
    val password: String
)