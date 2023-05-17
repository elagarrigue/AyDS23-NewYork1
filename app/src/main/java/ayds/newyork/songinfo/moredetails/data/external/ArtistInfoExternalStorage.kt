package ayds.newyork.songinfo.moredetails.data.external

import ayds.newyork.songinfo.moredetails.data.external.info.NYTArtistInfoServiceImpl
import ayds.newyork.songinfo.moredetails.domain.ArtistInformation

interface ArtistInfoExternalStorage {
    fun getArtistInfo(name: String): ArtistInformation?
}

class ArtistInfoExternalStorageImpl(private val nytArtistInfoService: NYTArtistInfoServiceImpl) :
    ArtistInfoExternalStorage {

    override fun getArtistInfo(name: String): ArtistInformation? {
        return nytArtistInfoService.getArtistInfo(name)
    }
}