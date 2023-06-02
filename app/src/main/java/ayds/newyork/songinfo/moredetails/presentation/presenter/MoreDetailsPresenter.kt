package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.DataRepository
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.observer.Subject
import ayds.observer.Observable

interface MoreDetailsPresenter {
    val uiStateObservable: Observable<MoreDetailsUIState>
    var uiState: MoreDetailsUIState
    fun getDataArtist(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val dataRepository: DataRepository,
    private val formatterInfo: FormatterInfo
) : MoreDetailsPresenter {
    private val onUIStateSubject = Subject<MoreDetailsUIState>()
    override val uiStateObservable = onUIStateSubject
    override var uiState: MoreDetailsUIState = MoreDetailsUIState()
    private lateinit var dataArtistCards: MutableList<Card>
    override fun getDataArtist(artistName: String) = Thread {
        dataArtistCards = dataRepository.getDataByTerm(artistName)
        updateUiState(dataArtistCards, artistName)
    }.start()

    private fun updateUiState(data: MutableList<Card>, artistName: String) {
        uiState.dataCards.clear()
        for (card in data) {
            if (card is Card.DataCard) {
                val descriptionFormatted =
                    formatterInfo.buildCardDescription(
                        card.description,
                        artistName,
                        card.isLocallyStored
                    )
                card.copy(
                    descriptionFormatted,
                    card.infoUrl,
                    card.source,
                    card.sourceLogoUrl,
                    card.isLocallyStored
                )
            }
            uiState.dataCards.add(card)
        }
        uiStateObservable.notify(uiState)
    }
}