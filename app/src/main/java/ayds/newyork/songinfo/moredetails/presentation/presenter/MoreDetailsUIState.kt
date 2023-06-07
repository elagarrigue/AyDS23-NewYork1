package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.Card

data class MoreDetailsUIState(
    val dataCards: List<Card> = listOf()
)