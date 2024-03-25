package com.vd.study.core.presentation.utils

import android.graphics.Color
import android.widget.Button
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.vd.study.core.R

fun TextInputLayout.setDarkTheme(editText: TextInputEditText) {
    defaultHintTextColor = ContextCompat.getColorStateList(context, R.color.white)
    boxStrokeColor = ContextCompat.getColor(context, R.color.white)
    editText.backgroundTintList = ContextCompat.getColorStateList(context, R.color.second_background)
    editText.setTextColor(Color.WHITE)
}

fun Button.setDarkTheme() {
    val background = ContextCompat.getColorStateList(context, R.color.second_background)
    backgroundTintList = background
    setTextColor(Color.WHITE)
}
