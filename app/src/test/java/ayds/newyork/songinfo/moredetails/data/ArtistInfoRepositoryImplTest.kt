package ayds.newyork.songinfo.moredetails.data


import ayds.newyork.songinfo.moredetails.domain.ArtistInformation
import ayds.newyork.songinfo.moredetails.data.external.ArtistInfoExternalStorage
import ayds.newyork.songinfo.moredetails.data.local.sqldb.ArtistInfoLocalStorageImpl
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ArtistInfoRepositoryImplTest {

    private val mockLocalStorage = mockk<ArtistInfoLocalStorageImpl>()
    private val mockExternalStorage = mockk<ArtistInfoExternalStorage>()
    private val repository = ArtistInfoRepositoryImpl(mockLocalStorage, mockExternalStorage)

    @Test
    fun `givin artis info in LocalStorage`() {
        val artistName = "test artist"
        val artistInfo = ArtistInformation.ArtistInformationData(
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
    fun `givin artis info not in LocalStorage`() {
        val artistName = "test artist"
        val artistInfo = ArtistInformation.ArtistInformationEmpty
        every { mockLocalStorage.getArtistInfo(artistName) } returns ArtistInformation.ArtistInformationEmpty
        every { mockExternalStorage.getArtistInfo(artistName) } returns artistInfo
        val result = repository.getArtistInfoByTerm(artistName)
        assertEquals(artistInfo, result)
    }

    @Test
    fun `givin artist info not in LocalStorage and exception occurs`() {
        val artistName = "test artist"
        every { mockLocalStorage.getArtistInfo(artistName) } returns ArtistInformation.ArtistInformationEmpty
        every { mockExternalStorage.getArtistInfo(artistName) } throws Exception()
        val result = repository.getArtistInfoByTerm(artistName)
        assertTrue(result is ArtistInformation.ArtistInformationEmpty)
    }


}
