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


internal class MoreDetailsPresenterImpl(private val dataRepository: DataRepository) : MoreDetailsPresenter {
    private val onUIStateSubject = Subject<MoreDetailsUIState>()
    override val uiStateObservable = onUIStateSubject
    override var uiState: MoreDetailsUIState = MoreDetailsUIState()
    private lateinit var dataArtistCards: MutableList<Card>
    override fun getDataArtist(artistName: String) = Thread {
        dataArtistCards = dataRepository.getDataByTerm(artistName)
        updateUiState(dataArtistCards)
    }.start()

    private fun updateUiState(data: MutableList<Card>) {
        uiState.dataCards.clear()
        for (card in data) {
            uiState.dataCards.add(card)
        }
        uiStateObservable.notify(uiState)
    }
}