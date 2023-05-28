package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.domain.DataRepository
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.moredetails.data.local.sqldb.DataLocalStorageImpl
import ayds.NY1.NewYorkTimes.external.DependenciesInjector
import ayds.NY1.NewYorkTimes.external.NYTArtistInfoService
import ayds.NY1.NewYorkTimes.external.entity.ArtistInformationExternal
import ayds.newyork.songinfo.moredetails.domain.Source

class DataRepositoryImpl(
    private val dataLocalStorage: DataLocalStorageImpl,
    private val broker: BrokerService,
) : DataRepository {
    private lateinit var data: Card

    override fun getDataByTerm(name: String): MutableList<Card> {
        //var dataLocal = dataLocalStorage.getData(name)
        var data: MutableList<Card> = mutableListOf()
        /*when (dataLocal) {
            is Card.DataCard -> {
                markDataAsLocal(dataLocal)
                data = dataLocal
            }
            else -> {*/
                try {
                    val artistInfo: ArtistInformationExternal? = broker.requestNYT(name)
                    val biography = broker.requestWikipedia(name)
                    val article = broker.requestLastFM(name)
                    if(artistInfo != null){
                        data.add(becomeToCard(artistInfo))
                    }
                    data.apply {add(becomeToCard(artistInfo))
                        add(becomeToCard(article))
                        add(becomeToCard(biography))}

                } catch (e: Exception) {
                    data = mutableListOf(Card.EmptyCard)

                }
            //}
        //}
        return data
    }

    private fun markDataAsLocal(data: Card.DataCard) {
        //data.isLocallyStored = true
    }

    companion object{
        fun becomeToCard(description: String?, infoUrl: String?, source: Source, sourceLogoUrl: String?): Card{
            return Card.DataCard(description, infoUrl, source, sourceLogoUrl)
        }
    }
}