package com.vd.study.core

import kotlinx.coroutines.runBlocking
import kotlin.Exception

sealed class Result<out T> {

    fun <R> map(mapper: (T) -> R): Result<R> {
        return runBlocking {
            suspendMap(mapper)
        }
    }

    abstract suspend fun <R> suspendMap(mapper: suspend (T) -> R): Result<R>

    abstract fun getOrNull(): T?

    abstract fun getOrException(): T

    object Progress : Result<Nothing>() {

        override suspend fun <R> suspendMap(mapper: suspend (Nothing) -> R): Result<R> {
            return this
        }

        override fun getOrNull(): Nothing? {
            return null
        }

        override fun getOrException(): Nothing {
            throw IllegalArgumentException("This component has not a value")
        }
    }

    class Error(private val exception: Exception) : Result<Nothing>() {

        override suspend fun <R> suspendMap(mapper: suspend (Nothing) -> R): Result<R> {
            return this
        }

        override fun getOrNull(): Nothing? {
            return null
        }

        override fun getOrException(): Nothing {
            throw exception
        }
    }

    class Correct<T>(private val value: T) : Result<T>() {

        override suspend fun <R> suspendMap(mapper: suspend (T) -> R): Result<R> {
            return try {
                Correct(mapper(value))
            } catch (exception: Exception) {
                Error(exception)
            }
        }

        override fun getOrNull(): T? {
            return value
        }

        override fun getOrException(): T {
            return value
        }
    }
}