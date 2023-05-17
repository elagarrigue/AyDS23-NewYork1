package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.ArtistInfoRepository
import ayds.newyork.songinfo.moredetails.domain.ArtistInformation
import ayds.observer.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class PresenterImplTest {

    private lateinit var presenter: PresenterImpl
    private lateinit var mockRepository: MockArtistInfoRepository
    private lateinit var uiStateObservable: Observable<MoreDetailsUIState>

    @Before
    fun setUp() {
        mockRepository = MockArtistInfoRepository()
        presenter = PresenterImpl(mockRepository)
        uiStateObservable = presenter.uiStateObservable
    }

    @Test
    fun `getArtistInfo with non-existing artist should update UI state to empty`() {
        val expectedUiState = MoreDetailsUIState()

        presenter.getArtistInfo("Non-existing artist")

        waitForUiThread()

        assertEquals(expectedUiState, presenter.uiState)
    }

    @Test
    fun `getArtistInfo with existing artist should update UI state to artist information`() {
        val artistName = "Madonna"
        val artistInfoData = ArtistInformation.ArtistInformationData(
            artistName = artistName,
            url = "https://www.madonna.com/",
            abstract = "Madonna is an American singer, songwriter, and actress.",
            isLocallyStored = false
        )
        val expectedUiState = MoreDetailsUIState(
            artistName = artistName,
            url = artistInfoData.url,
            abstract = artistInfoData.abstract,
            isLocallyStored = artistInfoData.isLocallyStored
        )

        mockRepository.artistInfo = artistInfoData

        presenter.getArtistInfo(artistName)

        waitForUiThread()

        assertEquals(expectedUiState, presenter.uiState)
    }

    private fun waitForUiThread() {
        val latch = CountDownLatch(1)
        uiStateObservable.subscribe {
            latch.countDown()
        }
        latch.await(2, TimeUnit.SECONDS)
    }

    private class MockArtistInfoRepository : ArtistInfoRepository {
        var artistInfo: ArtistInformation? = null

        override fun getArtistInfoByTerm(artistName: String): ArtistInformation? {
            return artistInfo
        }
    }
}