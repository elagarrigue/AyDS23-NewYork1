import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.moredetails.domain.DataRepository
import ayds.newyork.songinfo.moredetails.presentation.presenter.FormatterInfo
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenterImpl
import ayds.newyork.songinfo.moredetails.presentation.view.DEFAULT_IMAGE
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class MoreDetailsPresenterImplTest {
    private lateinit var dataRepository: DataRepository
    private lateinit var formatterInfo: FormatterInfo
    private lateinit var presenter: MoreDetailsPresenterImpl

    @Before
    fun setUp() {
        dataRepository = mockk()
        formatterInfo = mockk()
        presenter = MoreDetailsPresenterImpl(dataRepository, formatterInfo)
    }

    @Test
    fun `getDataArtist should update the UI state correctly`() {
        // Arrange
        val artistName = "Artist Name"
        val card1 = Card.DataCard(
            description = "Description 1",
            infoUrl = "",
            source = null,
            sourceLogoUrl = "https://example.com/logo1.png",
            isLocallyStored = true
        )
        val card2 = Card.DataCard(
            description = "Description 2",
            infoUrl = "",
            source = null,
            sourceLogoUrl = "https://example.com/logo2.png",
            isLocallyStored = false
        )
        val data = listOf(card1, card2)
        val expectedUpdatedDataCards = listOf(
            card1.copy(sourceLogoUrl = DEFAULT_IMAGE),
            card2
        )
        val expectedUpdatedUiState = presenter.uiState.copy(dataCards = expectedUpdatedDataCards)

        every { dataRepository.getDataByTerm(artistName) } returns data
        every { formatterInfo.buildCardDescription(any(), any(), any()) } returns "Formatted Description"

        presenter.getDataArtist(artistName)

        verify {
            dataRepository.getDataByTerm(artistName)
            presenter.uiStateObservable.notify(expectedUpdatedUiState)
        }

    }
}





