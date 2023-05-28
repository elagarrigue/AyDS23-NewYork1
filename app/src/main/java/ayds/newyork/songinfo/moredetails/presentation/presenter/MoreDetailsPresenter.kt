package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.DataRepository
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.observer.Subject
import ayds.observer.Observable

interface MoreDetailsPresenter {
    val uiStateObservable: Observable<MoreDetailsUIState>
    var uiState: MoreDetailsUIState
    fun getArtistInfo(artistName: String)
}

internal class MoreDetailsPresenterImpl(private val dataRepository: DataRepository) : MoreDetailsPresenter {
    private val onUIStateSubject = Subject<MoreDetailsUIState>()
    override val uiStateObservable = onUIStateSubject
    override var uiState: MoreDetailsUIState = MoreDetailsUIState()

    override fun getArtistInfo(artistName: String) = Thread {
        var artistInfo = dataRepository.getArtistInfoByTerm(artistName)
        updateUiState(artistInfo)
    }.start()

    private fun updateUiState(artistInfo: Card?) {
        when (artistInfo) {
            is Card.CardData -> updateArtistInfoUiState(artistInfo)
            Card.CardEmpty -> updateNoResultsUiState()
        }
        uiStateObservable.notify(uiState)
    }

    private fun updateArtistInfoUiState(artistInfoData: Card.CardData) {
        uiState = uiState.copy(
            artistName = artistInfoData.artistName,
            url = artistInfoData.url,
            abstract = artistInfoData.abstract,
            isLocallyStored = artistInfoData.isLocallyStored
        )
    }

    private fun updateNoResultsUiState() {
        uiState = uiState.copy(
            artistName = "",
            url = "",
            abstract = "",
            isLocallyStored = false
        )
    }
}