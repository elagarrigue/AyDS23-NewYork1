package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.domain.ArtistInfoRepository
import ayds.newyork.songinfo.moredetails.domain.ArtistInformation
import ayds.newyork.songinfo.moredetails.data.local.sqldb.ArtistInfoLocalStorageImpl
import ayds.NY1.NewYorkTimes.external.DependenciesInjector
import ayds.NY1.NewYorkTimes.external.NYTArtistInfoService
import ayds.NY1.NewYorkTimes.external.entity.ArtistInformationExternal

class ArtistInfoRepositoryImpl(
    private val artistInfoLocalStorage: ArtistInfoLocalStorageImpl,
    private var artistInfoExternalStorage: NYTArtistInfoService,
    private var artistInfo: ArtistInformation?,
) : ArtistInfoRepository {

    override fun getArtistInfoByTerm(name: String): ArtistInformation? {
        var artistInfoLocal = artistInfoLocalStorage.getArtistInfo(name)

        when (artistInfoLocal) {
            is ArtistInformation.ArtistInformationData -> {
                markArtistInfoAsLocal(artistInfoLocal)
                artistInfo = artistInfoLocal
            }
            else -> {
                try {
                    artistInfoExternalStorage = DependenciesInjector.init()
                    var artistInfoExternal = artistInfoExternalStorage.getArtistInfo(name)
                    when (artistInfoExternal) {
                        is ArtistInformationExternal.ArtistInformationDataExternal -> {
                            val artistInfoData = ArtistInformation.ArtistInformationData(
                                artistInfoExternal.artistName,
                                artistInfoExternal.abstract,
                                artistInfoExternal.url,
                                artistInfoExternal.isLocallyStored
                            )
                            artistInfoLocalStorage.saveArtistInfo(artistInfoData)
                            artistInfo = artistInfoData
                        }
                        else -> {
                            artistInfo = ArtistInformation.ArtistInformationEmpty
                        }
                    }
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