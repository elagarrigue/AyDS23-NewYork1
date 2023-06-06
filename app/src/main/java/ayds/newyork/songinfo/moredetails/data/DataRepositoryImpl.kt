package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.domain.DataRepository
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.moredetails.data.local.sqldb.DataLocalStorageImpl
import ayds.newyork.songinfo.moredetails.data.broker.BrokerService

class DataRepositoryImpl(
    private val dataLocalStorage: DataLocalStorageImpl,
    private val broker: BrokerService,
) : DataRepository {

    override fun getDataByTerm(name: String): List<Card> {
        val cardsLocal = dataLocalStorage.getData(name)
        return when {
            cardsLocal.isNotEmpty() -> {
                markDataAsLocal(cardsLocal)
                cardsLocal
            }
            else -> {
                try {
                    val cardsService = broker.getCards(name)
                    dataLocalStorage.saveData(cardsService, name)
                    cardsService
                } catch (e: Exception) {
                    listOf()
                }
            }
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



