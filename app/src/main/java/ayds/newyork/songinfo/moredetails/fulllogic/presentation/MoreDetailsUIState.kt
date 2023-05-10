package ayds.newyork.songinfo.moredetails.fulllogic.presentation

data class MoreDetailsUIState(
    val artistName: String = "",
    val url: String? = "",
    val abstract: String? = "",
    val isLocallyStored: Boolean = false
)
