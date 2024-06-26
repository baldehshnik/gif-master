package com.vd.study.core.presentation.utils

import android.util.Patterns
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.regex.Pattern
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
class PatternsModule {

    @Provides
    @Named("EMAIL_PATTERN")
    fun provideEmailPattern(): Pattern {
        return Patterns.EMAIL_ADDRESS
    }
}
