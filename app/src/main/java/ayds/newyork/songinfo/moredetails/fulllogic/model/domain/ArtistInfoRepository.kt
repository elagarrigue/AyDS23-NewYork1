package ayds.newyork.songinfo.moredetails.fulllogic.model.domain

interface ArtistInfoRepository {
    fun getArtistInfoByTerm(term: String): ArtistInformation?
}

