package com.vd.study.data.local.accounts.sources

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {

    @TypeConverter
    fun toDate(time: Long): Date {
        return Date(time)
    }

    @TypeConverter
    fun toTime(date: Date): Long {
        return date.time
    }
}
