package com.vd.study.core.global

import android.content.SharedPreferences

const val SIGN_IN_SHARED_PREFERENCES_NAME = "sign_in_preference"

const val IS_ACCOUNT_ENTERED_FIELD_NAME = "is_entered"
const val ACCOUNT_EMAIL_FIELD_NAME = "email"
const val ACCOUNT_ID_FIELD_NAME = "accountId"


const val APP_SHARED_PREFERENCES_NAME = "app_preference"

const val APP_THEME = "theme"
const val SHOW_NETWORK_WARNING = "network_warning"

fun SharedPreferences.setDefaultLoginValues() {
    this.edit()
        .putBoolean(IS_ACCOUNT_ENTERED_FIELD_NAME, false)
        .putString(ACCOUNT_EMAIL_FIELD_NAME, "")
        .putInt(ACCOUNT_ID_FIELD_NAME, -1)
        .apply()
}
