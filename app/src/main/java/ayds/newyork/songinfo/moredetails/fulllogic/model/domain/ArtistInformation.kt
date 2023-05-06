package ayds.newyork.songinfo.moredetails.fulllogic.model.domain

   data class ArtistInformation(
        val artistName: String,
        var abstract: String?,
        val url: String?,
        var isLocallyStored: Boolean = false
    )
