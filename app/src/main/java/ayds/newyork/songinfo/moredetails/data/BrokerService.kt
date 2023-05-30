package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.NY1.NewYorkTimes.external.DependenciesInjector
import ayds.NY1.NewYorkTimes.external.NYTArtistInfoService
import ayds.NY1.NewYorkTimes.external.entity.ArtistInformationExternal
import ayds.newyork.songinfo.moredetails.domain.Source
import lisboa4LastFM.LastFMInjector
import lisboa4LastFM.LastFMService
import lisboa4LastFM.ArtistBiography
import wikipedia.external.external.WikipediaInjector
import wikipedia.external.external.WikipediaArticleService
import wikipedia.external.external.entities.WikipediaArtist

interface BrokerService {
    fun requestNYT(artistName: String) :Card
    fun requestLastFM(artistName: String) :Card
    fun requestWikipedia(artistName: String) :Card
}

internal class BrokerServiceImpl(): BrokerService{
    private lateinit var artistInfoProvider: NYTArtistInfoService
    private lateinit var biographyProvider: LastFMService
    private lateinit var articlesProvider: WikipediaArticleService

    override fun requestNYT(artistName: String): Card {
        artistInfoProvider = DependenciesInjector.init()
        val dataInfo: ArtistInformationExternal? = artistInfoProvider.getArtistInfo(artistName)
        if(dataInfo is ArtistInformationExternal.ArtistInformationDataExternal){
            return becomeToCard(dataInfo.artistName,
                dataInfo.url,
                Source.NEW_YORK_TIMES,
                //PREGUNTAR DE DONDE SALE EL sourceLogoURL
                "https://es.wikipedia.org/wiki/Archivo:The_New_York_Times_Logo.svg#/media/Archivo:NewYorkTimes.svg",
                dataInfo.isLocallyStored
            )
        }else{
            return Card.EmptyCard
        }
    }

    override fun requestLastFM(artistName: String): Card {
        biographyProvider = LastFMInjector.getLastFmService()
        val dataBiography: ArtistBiography? = biographyProvider.getArtistBiography(artistName)
        if(dataBiography != null){
            return becomeToCard(dataBiography.artistInfo,
                dataBiography.url,
                Source.LAST_FM,
                //PREGUNTAR DE DONDE SALE EL sourceLogoURL
                ArtistBiography.URL_LAST_FM_IMAGE,
                dataBiography.isLocallyStored
            )
        }else{
            return Card.EmptyCard
        }
    }

    override fun requestWikipedia(artistName: String): Card {
        articlesProvider = WikipediaInjector.generateWikipediaService()
        val dataArtist: WikipediaArtist = articlesProvider.getArtist(artistName)
        if(dataArtist != null){
            return becomeToCard(dataArtist.artistInfo,
                dataArtist.wikipediaUrl,
                Source.WIKIPEDIA,
                //PREGUNTAR DE DONDE SALE EL sourceLogoURL
                "https://upload.wikimedia.org/wikipedia/commons/8/80/Wikipedia-logo-v2.svg",
                dataArtist.isInDataBase
            )
        }else{
            return Card.EmptyCard
        }
    }

    private fun becomeToCard(description: String?, infoUrl: String?, source: Source, sourceLogoUrl: String?, isLocally: Boolean): Card{
        return Card.DataCard(description, infoUrl, source, sourceLogoUrl, isLocally)
    }
}