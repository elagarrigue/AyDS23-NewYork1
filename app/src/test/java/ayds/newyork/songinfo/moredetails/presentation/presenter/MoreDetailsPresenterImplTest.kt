package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.ArtistInfoRepository
import ayds.newyork.songinfo.moredetails.domain.ArtistInformation
import ayds.observer.Observable
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

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
        val expectedUiState = MoreDetailsUIState()

        every { artistInfoRepository.getArtistInfoByTerm("Non-existing artist") } returns null

        moreDetailsPresenter.getArtistInfo("Non-existing artist")

        waitForUiThread()

        assertEquals(expectedUiState, moreDetailsPresenter.uiState)
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

        every { artistInfoRepository.getArtistInfoByTerm(artistName) } returns artistInfoData

        moreDetailsPresenter.getArtistInfo(artistName)

        waitForUiThread()

        assertEquals(expectedUiState, moreDetailsPresenter.uiState)
    }

    private fun waitForUiThread() {
        val latch = CountDownLatch(1)
        uiStateObservable.subscribe {
            latch.countDown()
        }
        latch.await(2, TimeUnit.SECONDS)
    }
}

