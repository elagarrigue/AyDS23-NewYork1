package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.ArtistInfoRepository
import ayds.newyork.songinfo.moredetails.domain.ArtistInformation
import ayds.observer.Observable
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class MoreDetailsPresenterImplTest {

    private lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private lateinit var artistInfoRepository: ArtistInfoRepository
    private lateinit var uiStateObservable: Observable<MoreDetailsUIState>

    @Before
    fun setUp() {
        artistInfoRepository = mockk()
        moreDetailsPresenter = MoreDetailsPresenterImpl(artistInfoRepository)
        uiStateObservable = moreDetailsPresenter.uiStateObservable
    }

    @Test
    fun `getArtistInfo with non-existing artist should update UI state to empty`() {
        val artist = ArtistInformation.ArtistInformationEmpty
        val artistName = "Artist empty"
        val expectedUiState = MoreDetailsUIState(
            "",
            "",
            "",
            false
        )
        val uiStateTester: (MoreDetailsUIState) -> Unit = mockk(relaxed =true)
        moreDetailsPresenter.uiStateObservable.subscribe { uiStateTester(it) }

        every { artistInfoRepository.getArtistInfoByTerm(artistName) } returns artist

        moreDetailsPresenter.getArtistInfo(artistName)

        verify { uiStateTester(expectedUiState) }

        assertEquals(expectedUiState, moreDetailsPresenter.uiState)
    }

    @Test
    fun `getArtistInfo with existing artist should update UI state to artist information`() {
        val artistName = "Madonna"
        val artist = ArtistInformation.ArtistInformationData(
            artistName = artistName,
            url = "https://www.madonna.com/",
            abstract = "Madonna is an American singer, songwriter, and actress.",
            isLocallyStored = false
        )
        val expectedUiState = MoreDetailsUIState(
            artistName = artistName,
            url = artist.url,
            abstract = artist.abstract,
            isLocallyStored = artist.isLocallyStored
        )

        val uiStateTester: (MoreDetailsUIState) -> Unit = mockk(relaxed =true)
        moreDetailsPresenter.uiStateObservable.subscribe { uiStateTester(it) }

        every { artistInfoRepository.getArtistInfoByTerm(artistName) } returns artist

        moreDetailsPresenter.getArtistInfo(artistName)

        verify { uiStateTester(expectedUiState) }

        assertEquals(expectedUiState, moreDetailsPresenter.uiState)
    }

}



