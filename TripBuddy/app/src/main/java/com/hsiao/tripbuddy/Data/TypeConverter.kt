/*
Vincent Hsiao - CIS 135 Final Project
TypeConverter.kt

converts Date object to Long

 */

package com.hsiao.tripbuddy.Data

import androidx.room.TypeConverter
import java.util.Date

class TypeConverter {
    @TypeConverter
    fun toDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time.toLong()
    }
}