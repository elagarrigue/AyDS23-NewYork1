package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.DataRepository
import ayds.newyork.songinfo.moredetails.domain.Card
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
        for (card in data) {
            val updatedCard = if (card is Card.DataCard) {
                val descriptionFormatted = formatterInfo.buildCardDescription(
                    card.description,
                    artistName,
                    card.isLocallyStored
                )
                card.copy(
                    description = descriptionFormatted,
                    isLocallyStored = card.isLocallyStored
                )
            } else {
                card
            }
            updatedDataCards.add(updatedCard)
        }
        val updatedUiState = uiState.copy(dataCards = updatedDataCards)
        uiStateObservable.notify(updatedUiState)
    }


}