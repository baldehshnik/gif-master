package com.vd.study.core.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Dispatchers @Inject constructor(

    @MainDispatcher
    private val _mainDispatcher: MainCoroutineDispatcher,

    @IODispatcher
    private val _ioDispatcher: CoroutineDispatcher,

    @DefaultDispatcher
    private val _defaultDispatcher: CoroutineDispatcher
) {

    val mainDispatcher: MainCoroutineDispatcher get() = _mainDispatcher
    val ioDispatcher: CoroutineDispatcher get() = _ioDispatcher
    val defaultDispatcher: CoroutineDispatcher get() = _defaultDispatcher

}