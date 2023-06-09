package ayds.newyork.songinfo.moredetails.data.broker.proxy

import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.moredetails.domain.Source
import wikipedia.external.external.WikipediaArticleService
import wikipedia.external.external.entities.WikipediaArtist

class WikipediaProxyImpl (private val articlesProvider: WikipediaArticleService) : Proxy {
    override fun request(artistName: String): Card {
        return try {
            val dataArtist: WikipediaArtist = articlesProvider.getArtist(artistName)
            return mapToCard(dataArtist)
        } catch (e: Exception) {
            Card.EmptyCard
        }
    }

    private fun mapToCard(dataArtist: WikipediaArtist): Card {
        return if (dataArtist != null) {
            Card.DataCard(
                dataArtist.artistInfo,
                dataArtist.wikipediaUrl,
                Source.WIKIPEDIA,
                "https://upload.wikimedia.org/wikipedia/commons/8/80/Wikipedia-logo-v2.svg",
                dataArtist.isInDataBase
            )
        } else {
            Card.EmptyCard
        }
    }


}