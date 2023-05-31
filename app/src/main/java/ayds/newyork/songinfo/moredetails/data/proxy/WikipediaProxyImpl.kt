package ayds.newyork.songinfo.moredetails.data.proxy

import ayds.newyork.songinfo.moredetails.data.Proxy
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.moredetails.domain.Source
import wikipedia.external.external.WikipediaInjector
import wikipedia.external.external.entities.WikipediaArtist

class WikipediaProxyImpl :Proxy{
    override fun request(artistName: String): Card {
        val articlesProvider = WikipediaInjector.generateWikipediaService()
        val dataArtist: WikipediaArtist = articlesProvider.getArtist(artistName)
        return becomeToCard(dataArtist)
    }

    private fun becomeToCard(dataArtist: WikipediaArtist): Card {
        if(dataArtist != null){
            return Card.DataCard(dataArtist.artistInfo,
                dataArtist.wikipediaUrl,
                Source.WIKIPEDIA,
                "https://upload.wikimedia.org/wikipedia/commons/8/80/Wikipedia-logo-v2.svg",
                dataArtist.isInDataBase
            )
        }else{
            return Card.EmptyCard
        }
    }


}