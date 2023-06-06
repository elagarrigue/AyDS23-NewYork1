package ayds.newyork.songinfo.moredetails.data.broker.proxy

import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.moredetails.domain.Source
import lisboa4LastFM.ArtistBiography
import lisboa4LastFM.LastFMService

class LastFMProxyImpl (private val biographyProvider: LastFMService) : Proxy {
    override fun request(artistName: String): Card {
        val dataBiography: ArtistBiography? = biographyProvider.getArtistBiography(artistName)
        return mapToCard(dataBiography)
    }

    private fun mapToCard(dataBiography: ArtistBiography?): Card {
        return if (dataBiography != null) {
            Card.DataCard(
                dataBiography.artistInfo,
                dataBiography.url,
                Source.LAST_FM,
                ArtistBiography.URL_LAST_FM_IMAGE,
                dataBiography.isLocallyStored
            )
        } else {
            Card.EmptyCard
        }
    }
}