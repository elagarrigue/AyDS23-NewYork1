package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.NY1.NewYorkTimes.external.DependenciesInjector
import ayds.NY1.NewYorkTimes.external.info.NYTArtistInfoServiceImpl
import ayds.NY1.NewYorkTimes.external.entity.ArtistInformationExternal
import lisboa4LastFM.LastFMInjector
import lisboa4LastFM.LastFMService
import lisboa4LastFM.ArtistBiography
import wikipedia.external.external.WikipediaInjector
import wikipedia.external.external.WikipediaArticleService
import wikipedia.external.external.entities.WikipediaArtist

interface BrokerService {
    fun requestNYT(artistName: String) :ArtistInformationExternal?
    fun requestLastFM(artistName: String) :ArtistBiography
    fun requestWikipedia(artistName: String) :WikipediaArtist
}

internal class BrokerServiceImpl(): BrokerService{
    private lateinit var artistInfoProvider: NYTArtistInfoServiceImpl
    private lateinit var biographyProvider: LastFMService
    private lateinit var articlesProvider: WikipediaArticleService

    override fun requestNYT(artistName: String): ArtistInformationExternal? {
        artistInfoProvider = DependenciesInjector.init()
        return artistInfoProvider?.getArtistInfo(artistName) ?:
            throw IllegalStateException("ArtistInfoProvider is null")
    }

    override fun requestLastFM(artistName: String): ArtistBiography {
        biographyProvider = LastFMInjector.getLastFmService()
        return biographyProvider.getArtistBiography(artistName) ?:
            throw IllegalStateException("Artist biography is null")
    }

    override fun requestWikipedia(artistName: String): WikipediaArtist {
        articlesProvider = WikipediaInjector.generateWikipediaService()
        return articlesProvider.getArtist(artistName)
    }



}