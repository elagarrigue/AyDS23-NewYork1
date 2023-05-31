package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.domain.DataRepository
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.moredetails.data.local.sqldb.DataLocalStorageImpl

class DataRepositoryImpl(
    private val dataLocalStorage: DataLocalStorageImpl,
    private val broker: BrokerService,
) : DataRepository {

    override fun getDataByTerm(name: String): MutableList<Card> {
        var dataLocal = dataLocalStorage.getData(name)
        var data: MutableList<Card> = mutableListOf()
        if (dataLocal.isNotEmpty()) {
            markDataAsLocal(dataLocal)
            data = dataLocal
        } else {
            try {
                data = broker.requestToProxys(name)
                dataLocalStorage.saveData(data)
            } catch (e: Exception) {
                data = mutableListOf(Card.EmptyCard)
            }
        }
        return data
    }

    private fun markDataAsLocal(data: MutableList<Card>) {
        for (card in data) {
            if(card is Card.DataCard) {
                card.isLocallyStored = true
            }
        }
    }
}



