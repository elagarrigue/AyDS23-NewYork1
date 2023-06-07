package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.data.local.sqldb.DataLocalStorageImpl
import ayds.newyork.songinfo.moredetails.data.broker.BrokerService
import ayds.newyork.songinfo.moredetails.domain.Card
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DataRepositoryImplTest {

    private lateinit var dataLocalStorage: DataLocalStorageImpl
    private lateinit var broker: BrokerService
    private lateinit var dataRepository: DataRepositoryImpl

    @Before
    fun setUp() {
        dataLocalStorage = mockk()
        broker = mockk()
        dataRepository = DataRepositoryImpl(dataLocalStorage, broker)
    }

    @Test
    fun getDataByTerm_localDataNotEmpty_returnsLocalData() {
        // Arrange
        val name = "test"
        val localCards = listOf(Card.DataCard("Local Data","",null,"",true))
        every { dataLocalStorage.getData(name) } returns localCards

        // Act
        val result = dataRepository.getDataByTerm(name)

        // Assert
        assertEquals(localCards, result)
    }

    @Test
    fun getDataByTerm_localDataEmpty_returnsServiceData() {
        // Arrange
        val name = "test"
        val serviceCards = listOf(Card.DataCard("Service Data","",null,"",false))
        every { dataLocalStorage.getData(name) } returns listOf()
        every { broker.getCards(name) } returns serviceCards

        // Act
        val result = dataRepository.getDataByTerm(name)

        // Assert
        assertEquals(serviceCards, result)
    }

    @Test
    fun getDataByTerm_serviceError_returnsEmptyList() {
        // Arrange
        val name = "test"
        every { dataLocalStorage.getData(name) } returns listOf()
        every { broker.getCards(name) } throws Exception()

        // Act
        val result = dataRepository.getDataByTerm(name)

        // Assert
        assertEquals(emptyList<Card>(), result)
    }
}

