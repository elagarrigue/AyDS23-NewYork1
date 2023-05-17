package ayds.newyork.songinfo.moredetails.data.external

import ayds.newyork.songinfo.moredetails.domain.ArtistInformation

interface NYTArtistInfoService {

    fun getArtistInfo(name: String): ArtistInformation?
}