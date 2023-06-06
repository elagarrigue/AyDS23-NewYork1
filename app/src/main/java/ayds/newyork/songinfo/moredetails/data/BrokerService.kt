package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.domain.Card

interface BrokerService {
    fun getCards(artistName: String): List<Card>
}

internal class BrokerServiceImpl(private val proxys: List<Proxy>) : BrokerService {
    override fun getCards(artistName: String): List<Card> {
        var cardList: MutableList<Card> = mutableListOf()
        for (proxy in proxys) {
            var proxyCard : Card = proxy.request(artistName)
            if( proxyCard is Card.DataCard) {
                cardList.add(proxyCard)
            }
        }
        return cardList.toList()
    }
}