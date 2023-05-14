package ayds.newyork.songinfo.moredetails.domain

sealed class ArtistInformation {

    data class ArtistInformationData (

        val artistName: String,
        var abstract: String?,
        val url: String?,
        var isLocallyStored: Boolean = false
    ) : ArtistInformation()

    object ArtistInformationEmpty : ArtistInformation()

}