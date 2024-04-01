package com.vd.study.data.local

import com.vd.study.core.container.Result
import com.vd.study.data.exceptions.UnknownException

inline fun executeDatabaseUpdating(
    operation: () -> Long, error: Result.Error
): Result<Boolean> {
    return try {
        val updatedId = operation()
        if (updatedId == 0.toLong()) {
            error
        } else {
            Result.Correct(true)
        }
    } catch (e: Exception) {
        Result.Error(UnknownException())
    }
}

inline fun <T> catchReadingExceptionOf(operation: () -> T): Result<T> {
    return try {
        Result.Correct(operation())
    } catch (e: Exception) {
        Result.Error(UnknownException())
    }
}
