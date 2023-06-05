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
        val dataLocal = dataLocalStorage.getData(name)
        if (dataLocal.isNotEmpty()) {
            markDataAsLocal(dataLocal)
            return dataLocal
        }

        return try {
            val data = broker.getCards(name)
            dataLocalStorage.saveData(data, name)
            data
        } catch (e: Exception) {
            val card = Card.DataCard("NO RESULTS", "", Source.UNKNOWN, "", false)
            listOf(card)
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



