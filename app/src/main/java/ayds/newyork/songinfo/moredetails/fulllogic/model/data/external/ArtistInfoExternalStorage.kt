package ayds.newyork.songinfo.moredetails.fulllogic.model.data.external

import ayds.newyork.songinfo.moredetails.fulllogic.model.data.external.info.NYTArtistInfoServiceImpl
import ayds.newyork.songinfo.moredetails.fulllogic.model.domain.ArtistInformation

class ArtistInfoExternalStorage(private val nytArtistInfoService: NYTArtistInfoServiceImpl) {

    fun getArtistInfo(name: String): ArtistInformation? {

        return nytArtistInfoService.getArtistInfo(name)
    }
}