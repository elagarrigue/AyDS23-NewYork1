package ayds.newyork.songinfo.moredetails.domain

sealed class Card {
    abstract val description: String?
    abstract val infoUrl: String?
    abstract val source: Source
    abstract val sourceLogoUrl: String?

    data class DataCard(
        override val description: String?,
        override val infoUrl: String?,
        override val source: Source,
        override val sourceLogoUrl: String?
    ) : Card()

    object EmptyCard : Card() {
        override val description: String? = null
        override val infoUrl: String? = null
        override val source: Source = Source.UNKNOWN
        override val sourceLogoUrl: String? = null
    }
}

enum class Source {
    LAST_FM,
    WIKIPEDIA,
    NEW_YORK_TIMES,
    UNKNOWN
}