package ayds.newyork.songinfo.moredetails.fulllogic.model.data

import ayds.newyork.songinfo.moredetails.fulllogic.model.domain.ArtistInfoRepository
import ayds.newyork.songinfo.moredetails.fulllogic.model.domain.ArtistInformation
import ayds.newyork.songinfo.moredetails.fulllogic.model.data.external.ArtistInfoExternalStorage
import ayds.newyork.songinfo.moredetails.fulllogic.model.data.local.sqldb.ArtistInfoLocalStorageImpl


internal class ArtistInfoRepositoryImpl(
    private val artistInfoLocalStorage: ArtistInfoLocalStorageImpl,
    private val artistInfoExternalStorage: ArtistInfoExternalStorage,
) : ArtistInfoRepository {

    override fun getArtistInfoByTerm(name: String): ArtistInformation? {
        var artistInfo = artistInfoLocalStorage.getArtistInfo(name)

        when {
            artistInfo != null -> markArtistInfoAsLocal(artistInfo)
            else -> {
                try {
                    artistInfo = artistInfoExternalStorage.getArtistInfo(name)

                    artistInfo?.let {
                        when {
                            isSavedArtistInfo(name) -> artistInfoLocalStorage.getArtistInfo(name)
                            else -> artistInfoLocalStorage.saveArtistInfo(artistInfo!!)
                        }
                    }
                } catch (e: Exception) {
                    artistInfo = null
                }
            }
        }

        return artistInfo ?: null
    }

    private fun isSavedArtistInfo(name: String) = artistInfoLocalStorage.getArtistInfo(name) != null

    private fun markArtistInfoAsLocal(artistInfo: ArtistInformation) {
        artistInfo.isLocallyStored = true
    }
}