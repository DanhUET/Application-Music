package pro.branium.infrastructure.db

import androidx.room.TypeConverter
import java.util.Date

object DateConverter {
    @TypeConverter
    fun fromDate(date: Date?): Long {
        return date?.time ?: 0
    }

    @TypeConverter
    fun toDate(millis: Long): Date {
        return Date(millis)
    }
}