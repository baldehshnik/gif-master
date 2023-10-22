package com.vd.study.sign_in.presentation.adapter

import com.vd.study.sign_in.domain.entities.AccountEntity

interface OnRegisteredAccountClickListener {
    fun onClick(account: AccountEntity)
}