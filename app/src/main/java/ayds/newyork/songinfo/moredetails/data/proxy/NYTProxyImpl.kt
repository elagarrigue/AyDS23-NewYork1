package ayds.newyork.songinfo.moredetails.data.proxy

import ayds.ny1.newyorktimes.DependenciesInjector
import ayds.ny1.newyorktimes.entity.ArtistInformationExternal
import ayds.newyork.songinfo.moredetails.data.Proxy
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.moredetails.domain.Source

class NYTProxyImpl : Proxy {

    override fun request(artistName: String): Card {
        val artistInfoProvider = DependenciesInjector.init()
        val dataInfo: ArtistInformationExternal? = artistInfoProvider.getArtistInfo(artistName)
        return becomeToCard(dataInfo)
    }

    private fun becomeToCard(dataInfo: ArtistInformationExternal?): Card {
        if (dataInfo is ArtistInformationExternal.ArtistInformationDataExternal) {
            return Card.DataCard(
                dataInfo.artistName,
                dataInfo.url,
                Source.NEW_YORK_TIMES,
                dataInfo.logoUrl,
                dataInfo.isLocallyStored
            )
        } else {
            return Card.EmptyCard
        }
    }

}