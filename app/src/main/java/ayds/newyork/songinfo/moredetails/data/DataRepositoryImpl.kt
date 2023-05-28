package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.domain.DataRepository
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.moredetails.data.local.sqldb.DataLocalStorageImpl
import ayds.NY1.NewYorkTimes.external.DependenciesInjector
import ayds.NY1.NewYorkTimes.external.NYTArtistInfoService
import ayds.NY1.NewYorkTimes.external.entity.ArtistInformationExternal

class DataRepositoryImpl(
    private val dataLocalStorage: DataLocalStorageImpl,
    private val broker: BrokerService,
) : DataRepository {
    private lateinit var data: Card

    override fun getDataByTerm(name: String): Card {
        var dataLocal = dataLocalStorage.getData(name)

        when (dataLocal) {
            is Card.DataCard -> {
                markDataAsLocal(dataLocal)
                data = dataLocal
            }
            else -> {
                try {
                    dataExternalStorage = DependenciesInjector.init()
                    var dataExternal = dataExternalStorage.getArtistInfo(name)
                    when (dataExternal) {
                        is ArtistInformationExternal.ArtistInformationDataExternal -> {
                            val artistInfoData = Card.DataCard(
                                dataExternal.description,
                                dataExternal.infoUrl,
                                dataExternal.source,
                                dataExternal.sourceLogoUrl
                            )
                            dataLocalStorage.saveData(artistInfoData)
                            data = artistInfoData
                        }
                        else -> {
                            data = Card.EmptyCard
                        }
                    }
                } catch (e: Exception) {
                    data = Card.EmptyCard

                }
            }
        }
        return data
    }

    private fun markDataAsLocal(data: Card.DataCard) {
        //data.isLocallyStored = true
    }


}