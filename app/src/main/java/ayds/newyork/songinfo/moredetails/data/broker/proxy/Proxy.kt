package ayds.newyork.songinfo.moredetails.data.broker.proxy

import ayds.newyork.songinfo.moredetails.domain.Card

interface Proxy {
    fun request(artistName: String): Card
}