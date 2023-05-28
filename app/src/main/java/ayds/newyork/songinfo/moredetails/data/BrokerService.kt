package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.NY1.NewYorkTimes.external.DependenciesInjector
import ayds.NY1.NewYorkTimes.external.info.NYTArtistInfoServiceImpl
import lisboa4LastFM.LastFMInjector
import lisboa4LastFM.LastFMService
import wikipedia.external.external.WikipediaInjector
import wikipedia.external.external.WikipediaArticleService

interface BrokerService {
    fun requestNYT(artistName: String) :Card
    fun requestLastFM(artistName: String) : Card
    fun requestWikipedia(artistName: String) :Card
}

internal class BrokerServiceImpl(): BrokerService{
    private lateinit var artistInfoProvider: NYTArtistInfoServiceImpl
    private lateinit var biographyProvider: LastFMService
    private lateinit var articlesProvider: WikipediaArticleService

    override fun requestNYT(artistName: String): Card {
        artistInfoProvider = DependenciesInjector.init();

        return Card.EmptyCard

    }

    override fun requestLastFM(artistName: String): Card {
        biographyProvider = LastFMInjector.getLastFmService()

        return Card.EmptyCard
    }

    override fun requestWikipedia(artistName: String): Card {
        articlesProvider = WikipediaInjector.generateWikipediaService()

        return Card.EmptyCard
    }


}