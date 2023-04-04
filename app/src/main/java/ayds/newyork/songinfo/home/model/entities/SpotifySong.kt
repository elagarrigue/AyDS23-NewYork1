package ayds.newyork.songinfo.home.model.entities

sealed class Song {

    data class SpotifySong(
        val id: String,
        val songName: String,
        val artistName: String,
        val albumName: String,
        val releaseDate: String,
        val spotifyUrl: String,
        val imageUrl: String,
        var releaseDatePrecision: String,
        var isLocallyStored: Boolean = false

    ) : Song() {

       private var lanzamiento: Array<String> = releaseDate.split("-").toTypedArray()
        fun fechaLanzamiento () : String {
            return when (releaseDatePrecision) {
                 "day" -> "Release Date: " + lanzamiento[2] + "/" + lanzamiento[1] + "/" + lanzamiento[0]
                 "month" -> "Release Date: " + mesEquivalencia(lanzamiento[1]) + ", " + lanzamiento[0]
                 "year" -> "Release Date: " +lanzamiento[0] + " ( " + esBisiesto(lanzamiento[0]) + " )"
                else -> {""}
            }

        }
        private fun mesEquivalencia (mes: String) : String {
            return when (mes) {
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
        private fun esBisiesto (anio: String) : String {
            val anioInt = anio.toInt()
            return if (( anioInt % 4 == 0) && ((anioInt % 100 != 0) || (anioInt % 400 == 0)))
                    "Leap year"
            else "Not a leap year"
        }
    }

    object EmptySong : Song()
}