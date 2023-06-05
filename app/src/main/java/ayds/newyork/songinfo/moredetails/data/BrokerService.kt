package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.domain.Card

interface BrokerService {
    fun getCards(artistName: String): List<Card>
}

internal class BrokerServiceImpl(private val proxys: List<Proxy>) : BrokerService {
    override fun getCards(artistName: String): List<Card> {
        var cardList: MutableList<Card> = mutableListOf()
        for (proxy in proxys) {
            cardList.add(proxy.request(artistName))
        }
        return cardList.toList()
    }
}