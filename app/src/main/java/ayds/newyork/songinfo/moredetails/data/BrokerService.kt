package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.domain.Card

interface BrokerService {
    fun requestToProxys(artistName: String): MutableList<Card>
}

internal class BrokerServiceImpl(private val proxys: MutableList<Proxy>) : BrokerService {
    override fun requestToProxys(artistName: String): MutableList<Card> {
        var cardList: MutableList<Card> = mutableListOf()
        for (proxy in proxys) {
            cardList.add(proxy.request(artistName))
        }
        return cardList
    }

}