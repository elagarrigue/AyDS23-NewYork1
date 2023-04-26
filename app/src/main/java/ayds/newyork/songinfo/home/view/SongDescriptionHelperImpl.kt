package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.home.model.entities.Song.EmptySong
import ayds.newyork.songinfo.home.model.entities.Song
import ayds.newyork.songinfo.home.model.entities.Song.SpotifySong

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}
internal class SongDescriptionHelperImpl (private val formatDate: FormatterDate) : SongDescriptionHelper {
     lateinit var strategy : PrecisionFormatStrategy
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong -> {
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Release Date: ${obtainReleaseDate(song.releaseDatePrecision,song.releaseDate)}"
            }
            else -> "Song not found"
        }
    }
    private fun obtainReleaseDate(releaseDatePrecision: String, releaseDate: String): String {
        formatDate.setStrategy(defineStrategy(releaseDatePrecision))
        return formatDate.executeStrategy(releaseDate)
    }
    private fun defineStrategy(precision: String) : PrecisionFormatStrategy {
        return when (precision) {
            "day" -> DayFormatStrategy()
            "month" -> MonthFormatStrategy()
            "year" -> YearFormatStrategy()
            else -> DefaultStrategy()
        }
    }
}
