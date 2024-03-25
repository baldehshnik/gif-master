package com.vd.study.core.global

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeIdentifier @Inject constructor() {
    var isLightTheme = true
}
