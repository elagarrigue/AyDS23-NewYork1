package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.ArtistInfoRepository
import ayds.newyork.songinfo.moredetails.domain.ArtistInformation
import ayds.observer.Subject
import ayds.observer.Observable

interface Presenter {
    val uiStateObservable: Observable<MoreDetailsUIState>
    var uiState: MoreDetailsUIState
    fun getArtistInfo(artistName: String)
}

internal class PresenterImpl(private val artistInfoRepository: ArtistInfoRepository) : Presenter {
    private val onUIStateSubject = Subject<MoreDetailsUIState>()
    override val uiStateObservable = onUIStateSubject
    override var uiState: MoreDetailsUIState = MoreDetailsUIState()

    override fun getArtistInfo(artistName: String) = Thread {
        var artistInfo = artistInfoRepository.getArtistInfoByTerm(artistName)
        updateUiState(artistInfo)
    }.start()

    private fun updateUiState(artistInfo: ArtistInformation?) {
        when (artistInfo) {
            is ArtistInformation.ArtistInformationData -> updateArtistInfoUiState(artistInfo)
            ArtistInformation.ArtistInformationEmpty -> updateNoResultsUiState()
        }
        uiStateObservable.notify(uiState)
    }

    private fun updateArtistInfoUiState(artistInfoData: ArtistInformation.ArtistInformationData) {
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