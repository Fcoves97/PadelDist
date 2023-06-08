package converters

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.sql.Date
import java.sql.Time

object Converters {
    @TypeConverter
    fun fromLongToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun fromDateToLong(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromStringToDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }

    @TypeConverter
    fun fromDecimalToString(decimal: BigDecimal?): String? {
        return decimal?.toString()
    }

    @TypeConverter
    fun fromLongToTime(value: Long?): Time? {
        return value?.let { Time(it) }
    }

    @TypeConverter
    fun fromTimeToLong(time: Time?): Long? {
        return time?.time
    }
}