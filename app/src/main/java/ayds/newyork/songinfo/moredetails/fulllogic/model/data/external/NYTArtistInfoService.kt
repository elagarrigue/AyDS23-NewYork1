package ayds.newyork.songinfo.moredetails.fulllogic.model.data.external

import ayds.newyork.songinfo.moredetails.fulllogic.model.ArtistInformation

interface NYTArtistInfoService {

    fun getArtistInfo(name: String): ArtistInformation?
}