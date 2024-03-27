package com.vd.study.core.presentation.utils

import com.vd.study.core.R

const val MIN_PASSWORD_LENGTH = 6

const val USERNAME_REGEX_STRING = "^(?![_0-9]+\$)[a-zA-Z0-9_]{6,30}\$"
const val PASSWORD_REGEX_STRING = "^[a-zA-Z0-9@#\$%&+=!_-]{6,20}\$"

const val IMAGE_LOADING_TIMEOUT = 10_000

const val ACCOUNT_IMAGE_FILE_NAME = "profile_image.jpg"

fun getGradientsArray(): List<Int> {
    return listOf(
        R.drawable.placeholder_voilet_gradient,
        R.drawable.placeholder_red_gradient,
        R.drawable.placeholder_blue_gradient
    )
}
