package ayds.newyork.songinfo.moredetails.data.broker.proxy

import ayds.ny1.newyorktimes.entity.ArtistInformationExternal
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.moredetails.domain.Source
import ayds.ny1.newyorktimes.NYTArtistInfoService

class NYTProxyImpl (private val artistInfoProvider: NYTArtistInfoService): Proxy {

    override fun request(artistName: String): Card {
        return try {
            val dataInfo: ArtistInformationExternal? = artistInfoProvider.getArtistInfo(artistName)
            mapToCard(dataInfo)
        } catch (e: Exception) {
            Card.EmptyCard
        }
    }

    private fun mapToCard(dataInfo: ArtistInformationExternal?): Card {
        return if (dataInfo is ArtistInformationExternal.ArtistInformationDataExternal) {
            Card.DataCard(
                dataInfo.artistName,
                dataInfo.url,
                Source.NEW_YORK_TIMES,
                dataInfo.logoUrl,
                dataInfo.isLocallyStored
            )
        } else {
            Card.EmptyCard
        }
    }

}