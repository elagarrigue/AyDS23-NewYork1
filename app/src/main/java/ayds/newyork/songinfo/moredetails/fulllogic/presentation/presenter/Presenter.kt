package ayds.newyork.songinfo.moredetails.fulllogic.presentation.presenter

import ayds.newyork.songinfo.moredetails.fulllogic.model.domain.ArtistInfoRepository
import ayds.newyork.songinfo.moredetails.fulllogic.model.domain.ArtistInformation
import ayds.newyork.songinfo.moredetails.fulllogic.presentation.MoreDetailsUIState

interface Presenter {

    var uiState: MoreDetailsUIState

    fun getArtistInfo(artistName: String)

}

internal class PresenterImpl(private val artistInfoRepository: ArtistInfoRepository) : Presenter {

    override var uiState: MoreDetailsUIState = MoreDetailsUIState()

    override fun getArtistInfo(artistName: String) {
        var artistInfo = artistInfoRepository.getArtistInfoByTerm(artistName)
        updateUiState(artistInfo)
    }

    private fun updateUiState(artistInfo: ArtistInformation?) {
        when (artistInfo) {
            is ArtistInformation.ArtistInformationData -> updateArtistInfoUiState(artistInfo)
            ArtistInformation.ArtistInformationEmpty -> updateNoResultsUiState()
        }
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
