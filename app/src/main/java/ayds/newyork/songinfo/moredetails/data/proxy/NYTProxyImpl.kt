package ayds.newyork.songinfo.moredetails.data.proxy

import ayds.NY1.NewYorkTimes.external.DependenciesInjector
import ayds.NY1.NewYorkTimes.external.entity.ArtistInformationExternal
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
        if(dataInfo is ArtistInformationExternal.ArtistInformationDataExternal) {
            return Card.DataCard(
                dataInfo.artistName,
                dataInfo.url,
                Source.NEW_YORK_TIMES,
                "https://es.wikipedia.org/wiki/Archivo:The_New_York_Times_Logo.svg#/media/Archivo:NewYorkTimes.svg",
                dataInfo.isLocallyStored
            )
        }else {
            return Card.EmptyCard
        }
    }

}