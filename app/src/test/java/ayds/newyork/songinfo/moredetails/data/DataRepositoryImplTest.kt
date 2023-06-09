import ayds.newyork.songinfo.moredetails.data.DataRepositoryImpl
import ayds.newyork.songinfo.moredetails.data.local.sqldb.DataLocalStorageImpl
import ayds.newyork.songinfo.moredetails.data.broker.BrokerService
import ayds.newyork.songinfo.moredetails.domain.Card
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class DataRepositoryImplTest {

    private lateinit var dataLocalStorage: DataLocalStorageImpl
    private lateinit var broker: BrokerService
    private lateinit var dataRepository: DataRepositoryImpl

    @Before
    fun setUp() {
        dataLocalStorage = mockk(relaxed = true)
        broker = mockk(relaxed = true)
        dataRepository = DataRepositoryImpl(dataLocalStorage, broker)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test getDataByTerm with local data available`() {
        val name = "artist name"
        val localCards = listOf<Card>(mockk(relaxed = true))
        every { dataLocalStorage.getData(name) } returns localCards

        val result = dataRepository.getDataByTerm(name)

        assertEquals(localCards, result)
        verify(exactly = 1) { dataLocalStorage.getData(name) }
        verify(exactly = 0) { broker.getCards(name) }
        verify(exactly = 0) { dataLocalStorage.saveData(any(), any()) }
    }

    @Test
    fun `test getDataByTerm with no local data available`() {
        val name = "artist name"
        val serviceCards = listOf<Card>(mockk(relaxed = true))
        every { dataLocalStorage.getData(name) } returns emptyList()
        every { broker.getCards(name) } returns serviceCards
        every { dataLocalStorage.saveData(serviceCards, name) } just runs

        val result = dataRepository.getDataByTerm(name)

        assertEquals(serviceCards, result)
        verify(exactly = 1) { dataLocalStorage.getData(name) }
        verify(exactly = 1) { broker.getCards(name) }
        verify(exactly = 1) { dataLocalStorage.saveData(serviceCards, name) }
    }
}


