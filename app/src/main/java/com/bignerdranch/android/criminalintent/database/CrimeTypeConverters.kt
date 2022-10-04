package com.bignerdranch.android.criminalintent.database

import androidx.room.TypeConverter
import java.sql.Time
import java.time.LocalTime
import java.util.*

class CrimeTypeConverters {
    //convert the Date to Long type so Room can store it in the table
    @TypeConverter
    fun fromDate(date: Date?) : Long? {
        return date?.time
    }

    //convert it back to Date type (original type)
    @TypeConverter
    fun toDate(millisSinceEpoch: Long?) : Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }
}