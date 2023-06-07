import ayds.newyork.songinfo.moredetails.domain.DataRepository
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.moredetails.presentation.presenter.FormatterInfo
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenterImpl
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsUIState
import ayds.newyork.songinfo.moredetails.presentation.view.DEFAULT_IMAGE
import ayds.observer.Observable
import io.mockk.*
import org.junit.Test

class MoreDetailsPresenterImplTest {

    @Test
    fun getDataArtist_updateUiStateWithFormattedData() {
        // Arrange
        val artistName = "Test Artist"
        val dataCards = listOf(Card.DataCard("Test Description"))
        val dataRepository = mockk<DataRepository>()
        val formatterInfo = mockk<FormatterInfo>()
        val uiStateObservable = mockk<Observable<MoreDetailsUIState>>(relaxed = true)
        val presenter = MoreDetailsPresenterImpl(dataRepository, formatterInfo)
        presenter.uiStateObservable = uiStateObservable

        every { dataRepository.getDataByTerm(artistName) } returns dataCards
        every { formatterInfo.buildCardDescription(any(), any(), any()) } returns "Formatted Description"

        // Act
        presenter.getDataArtist(artistName)
        Thread.sleep(100) // Esperar a que se complete el hilo

        // Assert
        val expectedCard = Card.DataCard(
            "Formatted Description",
            DEFAULT_IMAGE,
            isLocallyStored = false
        )
        val expectedUiState = MoreDetailsUIState(dataCards = listOf(expectedCard))
        verify { uiStateObservable.notify(expectedUiState) }
    }
    @Test
   /* fun getDataArtist_updateUiStateWithDefaultImageWhenLogoUrlIsInvalid() {
        // Arrange
        val artistName = "Test Artist"
        val dataCards = listOf(
            Card.DataCard("Test Description","",null,"Invalid URL",false),
            Card.DataCard("Another Description", "",null,"Valid URL",false))

        every { dataRepository.getDataByTerm(artistName) } returns dataCards
        every { formatterInfo.buildCardDescription(any(), any(), any()) } returns "Formatted Description"

        // Act
        presenter.getDataArtist(artistName)
        Thread.sleep(100) // Esperar a que se complete el hilo

        // Assert
        val expectedCard1 = Card.DataCard("Formatted Description","",null, sourceLogoUrl = DEFAULT_IMAGE,false)
        val expectedCard2 = Card.DataCard("Formatted Description", "",null,sourceLogoUrl = "Valid URL",false)
        val expectedUiState = MoreDetailsUIState(dataCards = listOf(expectedCard1, expectedCard2))

        assertEquals(expectedUiState, presenter.uiState)
    }*/
}




