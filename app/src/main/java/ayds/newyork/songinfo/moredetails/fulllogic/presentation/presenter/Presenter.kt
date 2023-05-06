package ayds.newyork.songinfo.moredetails.fulllogic.presentation.presenter

import ayds.newyork.songinfo.moredetails.fulllogic.model.domain.ArtistInfoRepository
import ayds.newyork.songinfo.moredetails.fulllogic.model.domain.ArtistInformation

abstract class Presenter {

    abstract val artistInfoRepository : ArtistInfoRepository;
    fun getArtistInfo(artistName: String?) : ArtistInformation?{
       return artistName?.let { artistInfoRepository.getArtistInfoByTerm(it) }
    }
}