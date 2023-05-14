package ayds.newyork.songinfo.moredetails.domain

interface ArtistInfoRepository {
    fun getArtistInfoByTerm(term: String): ArtistInformation?
}

