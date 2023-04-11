package ayds.newyork.songinfo.home.view

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.util.Date.*

class FormatterDate {

    @RequiresApi(Build.VERSION_CODES.O)
    fun viewRelease (releaseDate: String, precision: String): String {
        return when (precision) {
            "day" -> dayString(releaseDate)
            "month" -> monthString(releaseDate)
            "year" -> yearString(releaseDate)
            else -> ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dayString (date: String) : String {
        val day = LocalDate.parse(date)
        return day.dayOfMonth.toString() + "/" + day.monthValue.toString() + "/" + day.year.toString()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun monthString (date: String) : String {
        val month = YearMonth.parse(date)
        return month.month.toString() +", " + month.year.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun yearString (date: String) : String {
        val year = Year.parse(date)
        return year.toString() + " ( " + isLeapYear(date) + " )"
    }
    private fun isLeapYear (year: String) : String {
        val yearInt = year.toInt()
        return if ((yearInt % 4 == 0) && ((yearInt % 100 != 0) || (yearInt % 400 == 0)))
            "Leap year"
        else "Not a leap year"
    }
}