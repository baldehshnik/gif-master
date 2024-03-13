package com.vd.study.core.presentation.animation

import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes

fun View.loadAnimation(@AnimRes animationResource: Int) {
    val animation = AnimationUtils.loadAnimation(this.context, animationResource)
    this.startAnimation(animation)
}