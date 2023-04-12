package ayds.newyork.songinfo.home.view

import java.text.SimpleDateFormat


@Suppress("DEPRECATION")
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
        // val day = LocalDate.parse(date)
        println(date)
        val fecha = SimpleDateFormat("yyyy-MM-dd")
            val print = fecha.parse(date)
        println(print.date.toString() + "/" + print.month.inc().toString() + "/" + print.year.plus(1900).toString())


        return  print.date.toString() + "/" + print.month.inc().toString() + "/" + print.year.plus(1900).toString()
    }

    private fun monthString (date: String) : String {
        println(date)
        var formatMes = SimpleDateFormat("MM")
        val fecha = formatMes.parse(date)
        formatMes = SimpleDateFormat("MMMM")
        println(buildString {
        append("fecha: ")
        append(fecha)
        })
        val month = formatMes.format(fecha)
        val formatAnio = SimpleDateFormat("yyyy")
        val fecha2 = formatAnio.parse(date)
        println(buildString {
            append("fecha2: ")
            append(fecha2)
        })
        val year = formatAnio.format(fecha2)
        println("$month, $year")

        return "$month, $year"
    }

    private fun yearString (date: String) : String {
        //val year = Year.parse(date)
        return "" //year.toString() + " ( " + isLeapYear(date) + " )"
    }
    private fun isLeapYear (year: String) : String {
        val yearInt = year.toInt()
        return if ((yearInt % 4 == 0) && ((yearInt % 100 != 0) || (yearInt % 400 == 0)))
            "Leap year"
        else "Not a leap year"
    }
}