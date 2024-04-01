package com.vd.study.core.presentation.regex

import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.vd.study.core.presentation.utils.PASSWORD_REGEX_STRING
import com.vd.study.core.presentation.utils.USERNAME_REGEX_STRING

fun isPasswordInputCorrect(editText: TextInputEditText): Boolean {
    return PASSWORD_REGEX_STRING.toRegex()
        .matches(editText.text?.toString() ?: return false)
}

fun isEmailInputCorrect(editText: TextInputEditText): Boolean {
    return Patterns.EMAIL_ADDRESS
        .matcher(editText.text?.toString() ?: return false)
        .matches()
}

fun isUsernameInputCorrect(editText: TextInputEditText): Boolean {
    return editText.text?.toString()?.matches(USERNAME_REGEX_STRING.toRegex()) != false
}
