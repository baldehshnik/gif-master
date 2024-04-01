package com.vd.study.core.presentation.resources

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.getColorValue(@ColorRes colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}

fun Context.getColorStateListValue(@ColorRes colorRes: Int): ColorStateList? {
    return ContextCompat.getColorStateList(this, colorRes)
}

fun Context.getDrawableValue(@DrawableRes drawableRes: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableRes)
}
