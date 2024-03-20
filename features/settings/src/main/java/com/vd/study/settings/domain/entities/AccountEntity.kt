package com.vd.study.settings.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class AccountEntity(
    val id: Int,
    val username: String,
    val avatarUrl: String,
    val email: String,
    val password: String,
    val date: Date
): Parcelable
