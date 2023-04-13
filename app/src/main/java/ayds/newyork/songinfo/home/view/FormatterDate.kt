package ayds.newyork.songinfo.home.view

import java.text.SimpleDateFormat
import java.util.*

class FormatterDate {
    fun viewRelease (releaseDate: String, precision: String): String {
        return when (precision) {
            "day" -> dayString(releaseDate)
            "month" -> monthString(releaseDate)
            "year" -> yearString(releaseDate)
            else -> ""
        }
    }
    private fun dayString (date: String) : String {
        val dayFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateWithoutFormat = dayFormat.parse(date)
        return  dateWithoutFormat.date.toString() + "/" +
                dateWithoutFormat.month.inc().toString() + "/" +
                dateWithoutFormat.year.plus(1900).toString()
    }
    private fun monthString (date: String) : String {
        var monthFormat = SimpleDateFormat("yyyy-MM")
        var dateWithoutFormat = monthFormat.parse(date)
        monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
        val monthName = monthFormat.format(dateWithoutFormat)
        val yearFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)
        dateWithoutFormat = yearFormat.parse(date)
        val year = yearFormat.format(dateWithoutFormat)
        return "$monthName, $year"
    }
    private fun yearString (date: String) : String {
        val yearFormat = SimpleDateFormat("yyyy")
        val dateWithoutFormat = yearFormat.parse(date)
        val year = yearFormat.format(dateWithoutFormat)
        return "$year ( ${isLeapYear(date)} )"
    }
    private fun isLeapYear (year: String) : String {
        val yearInt = year.toInt()
        return if ((yearInt % 4 == 0) && ((yearInt % 100 != 0) || (yearInt % 400 == 0)))
            "Leap year"
        else "Not a leap year"
    }
}