package com.vd.study.sign_in.domain.entities

data class AccountEntity(
    val id: Int,
    val username: String,
    val avatarUrl: String,
    val email: String,
    val password: String
)
