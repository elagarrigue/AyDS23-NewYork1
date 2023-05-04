package ayds.newyork.songinfo.moredetails.fulllogic

data class ArtistInfo(
    val artistName: String,
    var abstract: String?,
    val url: String?,
    var isLocallyStored: Boolean = false
)
