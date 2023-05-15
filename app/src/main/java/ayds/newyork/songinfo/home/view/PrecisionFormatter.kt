package ayds.newyork.songinfo.home.view

import java.text.SimpleDateFormat
import java.util.*

interface PrecisionFormatStrategy {
    fun format(date: String): String
}
internal class DayFormatStrategy : PrecisionFormatStrategy  {
    override fun format(date: String): String {
        val dayFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateWithoutFormat = dayFormat.parse(date)
        val dayMonthYearFormat = SimpleDateFormat("dd/MM/yyyy")
        return dayMonthYearFormat.format(dateWithoutFormat)
    }
}
internal class MonthFormatStrategy : PrecisionFormatStrategy {
    override fun format(date: String): String {
        val monthYearFormat = SimpleDateFormat("yyyy-MM", Locale.ENGLISH)
        val dateWithoutFormat = monthYearFormat.parse(date)
        val dateFormat = SimpleDateFormat("MMMM, yyyy", Locale.ENGLISH)
        return dateFormat.format(dateWithoutFormat)
    }
}
internal class YearFormatStrategy : PrecisionFormatStrategy {
    override fun format(date: String): String {
        val yearFormat = SimpleDateFormat("yyyy")
        val dateWithoutFormat = yearFormat.parse(date)
        val year = yearFormat.format(dateWithoutFormat)
        return "$year (${leapYear(year)})"
    }
    private fun leapYear(year: String): String {
        val yearInt = year.toInt()
        return if (yearInt % 4 == 0 && (yearInt % 100 != 0 || yearInt % 400 == 0))
            "Leap year"
        else "Not a leap year"
    }
}
internal class DefaultStrategy : PrecisionFormatStrategy {
    override fun format(date: String) : String {
       return "The precision is not valid"
    }
}
