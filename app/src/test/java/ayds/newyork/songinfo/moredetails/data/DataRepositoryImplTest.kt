package ayds.newyork.songinfo.moredetails.data


import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.moredetails.data.external.ArtistInfoExternalStorage
import ayds.newyork.songinfo.moredetails.data.local.sqldb.DataLocalStorageImpl
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DataRepositoryImplTest {

    private val mockLocalStorage = mockk<DataLocalStorageImpl>()
    private val mockExternalStorage = mockk<ArtistInfoExternalStorage>()
    private val repository = DataRepositoryImpl(mockLocalStorage, mockExternalStorage)

    @Test
    fun `given artis info in LocalStorage`() {
        val artistName = "test artist"
        val artistInfo = Card.CardData(
            artistName,
            "test url",
            "test description",
            false
        )
        every { mockLocalStorage.getArtistInfo(artistName) } returns artistInfo

        val result = repository.getArtistInfoByTerm(artistName)

        assertEquals(artistInfo, result)
        assertTrue(artistInfo.isLocallyStored)
    }

    @Test
    fun `given artist info not in LocalStorage`() {
        val artistName = "test artist"
        val artistInfo = Card.CardData(
            artistName = "Test Artist",
            abstract = "Description",
            url = "https://example.com/artist"
        )
        every { mockLocalStorage.getArtistInfo(artistName) } returns null
        every { mockExternalStorage.getArtistInfo(artistName) } returns artistInfo

        val result = repository.getArtistInfoByTerm(artistName)

        assertEquals(artistInfo, result)

        verify { mockLocalStorage.saveArtistInfo(artistInfo) }
    }

    @Test
    fun `given artist info not in LocalStorage and exception occurs`() {
        val artistName = "test artist"
        every { mockLocalStorage.getArtistInfo(artistName) } returns Card.CardEmpty
        every { mockExternalStorage.getArtistInfo(artistName) } throws Exception()
        val result = repository.getArtistInfoByTerm(artistName)
        assertEquals(result, Card.CardEmpty)
    }


}
