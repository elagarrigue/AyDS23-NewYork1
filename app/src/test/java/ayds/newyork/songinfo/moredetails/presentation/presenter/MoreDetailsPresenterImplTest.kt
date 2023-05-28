package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.DataRepository
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.observer.Observable
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class MoreDetailsPresenterImplTest {

    private lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private lateinit var dataRepository: DataRepository
    private lateinit var uiStateObservable: Observable<MoreDetailsUIState>
    @Before
    fun setUp() {
        dataRepository = mockk()
        moreDetailsPresenter = MoreDetailsPresenterImpl(dataRepository)
        uiStateObservable = moreDetailsPresenter.uiStateObservable
    }

    @Test
    fun `getArtistInfo with non-existing artist should update UI state to empty`() {
        val artist = Card.CardEmpty
        val artistName = "Artist empty"
        val expectedUiState = MoreDetailsUIState(
            "",
            "",
            "",
            false
        )
        val uiStateTester: (MoreDetailsUIState) -> Unit = mockk(relaxed =true)
        moreDetailsPresenter.uiStateObservable.subscribe { uiStateTester(it) }

        every { dataRepository.getArtistInfoByTerm(artistName) } returns artist

        moreDetailsPresenter.getArtistInfo(artistName)

        verify { uiStateTester(expectedUiState) }
    }

    @Test
    fun `getArtistInfo with existing artist should update UI state to artist information`() {
        val artistName = "Madonna"
        val artist = Card.CardData(
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

        every { dataRepository.getArtistInfoByTerm(artistName) } returns artist

        moreDetailsPresenter.getArtistInfo(artistName)

        verify { uiStateTester(expectedUiState) }
    }
}



