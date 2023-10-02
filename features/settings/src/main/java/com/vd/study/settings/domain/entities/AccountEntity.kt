package com.vd.study.settings.domain.entities

data class AccountEntity(
    val id: Int,
    val username: String,
    val avatarUrl: String,
    val email: String,
    val password: String
)