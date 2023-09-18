package com.vd.study.data.local

import com.vd.study.core.Result
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