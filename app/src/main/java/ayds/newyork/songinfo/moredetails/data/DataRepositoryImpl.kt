package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.domain.DataRepository
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.moredetails.data.local.sqldb.DataLocalStorageImpl
import ayds.newyork.songinfo.moredetails.domain.Source

class DataRepositoryImpl(
    private val dataLocalStorage: DataLocalStorageImpl,
    private val broker: BrokerService,
) : DataRepository {

    override fun getDataByTerm(name: String): List<Card> {
        var dataLocal = dataLocalStorage.getData(name)
        if (dataLocal.isNotEmpty()) {
            markDataAsLocal(dataLocal)
            return dataLocal
        } else try {
            val data = broker.getCards(name)
            dataLocalStorage.saveData(data, name)
            return data
        } catch (e: Exception) {
            val card = Card.DataCard("NO RESULTS","",Source.UNKNOWN,"",false);
            val list = buildList<Card> { card }
            return list
        }
    }

    private fun markDataAsLocal(data: List<Card>) {
        for (card in data) {
            if (card is Card.DataCard) {
                card.isLocallyStored = true
            }
        }
    }
}



