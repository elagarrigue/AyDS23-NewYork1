package ayds.newyork.songinfo.moredetails.domain

sealed class Card {

    data class DataCard(
        val description: String,
        val infoUrl: String?,
        val source: Source?,
        val sourceLogoUrl: String,
        var isLocallyStored: Boolean = false
    ) : Card()

    object EmptyCard : Card()
}

enum class Source {
    LAST_FM,
    WIKIPEDIA,
    NEW_YORK_TIMES
}