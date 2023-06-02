package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.domain.Card

interface Proxy {

    fun request(artistName: String): Card

}