package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.DataRepository
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.moredetails.presentation.view.DEFAULT_IMAGE
import ayds.observer.Subject
import ayds.observer.Observable

interface MoreDetailsPresenter {
    val uiStateObservable: Observable<MoreDetailsUIState>
    val uiState: MoreDetailsUIState
    fun getDataArtist(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val dataRepository: DataRepository,
    private val formatterInfo: FormatterInfo
) : MoreDetailsPresenter {
    private val onUIStateSubject = Subject<MoreDetailsUIState>()
    override val uiStateObservable = onUIStateSubject
    override val uiState: MoreDetailsUIState = MoreDetailsUIState()
    override fun getDataArtist(artistName: String) = Thread {
        val dataArtistCards = dataRepository.getDataByTerm(artistName)
        updateUiState(dataArtistCards, artistName)
    }.start()

    private fun updateUiState(data: List<Card>, artistName: String) {
        val updatedDataCards = mutableListOf<Card>()
        if (data.isNotEmpty()) {
            for (card in data) {
                    if (card is Card.DataCard) {
                        val updatedCard =
                            card.copy(
                            description = formatCardDescription(card, artistName),
                            source = card.source,
                            sourceLogoUrl = card.sourceLogoUrl,
                            isLocallyStored = card.isLocallyStored,
                            infoUrl = card.infoUrl
                        )
                        updatedDataCards.add(updatedCard)
                    }
            }
        }
        else updatedDataCards.add(Card.DataCard("NOT FOUND RESULTS","",null, DEFAULT_IMAGE,false))

        val updatedUiState = uiState.copy(dataCards = updatedDataCards)
        uiStateObservable.notify(updatedUiState)
    }

    private fun formatCardDescription(card: Card.DataCard, artistName: String): String {
        return formatterInfo.buildCardDescription(
            card.description,
            artistName,
            card.isLocallyStored
        )
    }
}