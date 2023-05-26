package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.domain.ArtistInfoRepository
import ayds.newyork.songinfo.moredetails.domain.ArtistInformation
import ayds.newyork.songinfo.moredetails.data.local.sqldb.ArtistInfoLocalStorageImpl


class ArtistInfoRepositoryImpl(
    private val artistInfoLocalStorage: ArtistInfoLocalStorageImpl,
    private val artistInfoExternalStorage: NYTimes,
) : ArtistInfoRepository {

    override fun getArtistInfoByTerm(name: String): ArtistInformation? {
        var artistInfo = artistInfoLocalStorage.getArtistInfo(name)

        when {
            artistInfo is ArtistInformation.ArtistInformationData -> markArtistInfoAsLocal(
                artistInfo
            )
            else -> {
                try {
                    artistInfo = artistInfoExternalStorage.getArtistInfo(name)
                    artistInfoLocalStorage.saveArtistInfo(artistInfo as ArtistInformation.ArtistInformationData)
                } catch (e: Exception) {
                    artistInfo = ArtistInformation.ArtistInformationEmpty
                }
            }
        }

        return artistInfo
    }

    private fun markArtistInfoAsLocal(artistInfo: ArtistInformation.ArtistInformationData) {
        artistInfo.isLocallyStored = true
    }
}