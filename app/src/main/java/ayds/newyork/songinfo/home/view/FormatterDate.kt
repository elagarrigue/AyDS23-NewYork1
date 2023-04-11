package ayds.newyork.songinfo.home.view

class FormatterDate {

    fun viewRelease (releaseDate: String,precision: String) : String {
        var release: Array<String> = releaseDate.split("-").toTypedArray()
        return when (precision) {
            "day" -> release[2] + "/" + release[1] + "/" + release[0]
            "month" ->  monthString(release[1]) + ", " + release[0]
            "year" -> release[0] + " ( " + isLeapYear(release[0]) + " )"
            else -> {""}
        }

    }
    private fun monthString (month: String) : String {
        return when (month) {
            "01" -> "January"
            "02" -> "February"
            "03" -> "March"
            "04" -> "April"
            "05" -> "May"
            "06" -> "June"
            "07" -> "July"
            "08" -> "August"
            "09" -> "September"
            "10" -> "October"
            "11" -> "November"
            "12" -> "December"
            else -> {""}
        }
    }
    private fun isLeapYear (year: String) : String {
        val yearInt = year.toInt()
        return if (( yearInt % 4 == 0) && ((yearInt % 100 != 0) || (yearInt % 400 == 0)))
            "Leap year"
        else "Not a leap year"
    }
}