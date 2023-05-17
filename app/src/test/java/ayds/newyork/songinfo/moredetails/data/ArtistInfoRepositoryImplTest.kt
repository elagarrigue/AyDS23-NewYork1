package ayds.newyork.songinfo.moredetails.data


import ayds.newyork.songinfo.moredetails.domain.ArtistInformation
import ayds.newyork.songinfo.moredetails.data.external.ArtistInfoExternalStorage
import ayds.newyork.songinfo.moredetails.data.local.sqldb.ArtistInfoLocalStorageImpl
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class ArtistInfoRepositoryImplTest {

    private val mockLocalStorage = mockk<ArtistInfoLocalStorageImpl>()
    private val mockExternalStorage = mockk<ArtistInfoExternalStorage>()
    private val repository = ArtistInfoRepositoryImpl(mockLocalStorage, mockExternalStorage)

    @Test
    fun getArtistInfoByTerm_artistInfoInLocalStorage_returnsArtistInfo() {
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
    }

    @Test
    fun getArtistInfoByTerm_artistInfoNotInLocalStorage_returnsArtistInfo() {
        val artistName = "test artist"
        val artistInfo = ArtistInformation.ArtistInformationEmpty
        every { mockLocalStorage.getArtistInfo(artistName) } returns ArtistInformation.ArtistInformationEmpty
        every { mockExternalStorage.getArtistInfo(artistName) } returns artistInfo
        val result = repository.getArtistInfoByTerm(artistName)
        assertEquals(artistInfo, result)
    }

}
