package ayds.newyork.songinfo.moredetails.presentation.view

import ayds.newyork.songinfo.moredetails.presentation.presenter.FormatterInfo
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FormatterInfoTest {
    private lateinit var formatterInfo: FormatterInfo
    @Before
    fun setUp() {
        formatterInfo = FormatterInfo("John Doe")
    }
    @Test
    fun `with non-null abstract and local storage should return formatted text`() {
        val abstract = "This is the Bad Bunny's abstract"
        val expectedFormattedText = "<html><div width=400><font face=\"arial\">[*] This is the <b>BAD BUNNY</b>'s abstract</font></div></html>"
        val result = formatterInfo.buildCardDescription("Bad Bunny", abstract, true)
        assertEquals(expectedFormattedText, result)
    }
    @Test
    fun `with null abstract should return no results`() {
        val abstract = ""
        val isLocalStored = true
        val expectedNoResults = "No Results"
        val result = formatterInfo.buildCardDescription(formatterInfo.artistName, abstract, isLocalStored)
        assertEquals(expectedNoResults, result)
    }

    @Test
    fun `with empty abstract should return no results`() {
        val result = formatterInfo.buildCardDescription("Quevedo", "", true)
        val expected = "No Results"
        assertEquals(expected, result)
    }

    @Test
    fun `buildArtistInfoAbstract with non-local storage should return no results`() {
        val artistName = "John Doe"
        val abstract = "This is the John Doe's abstract"
        val isLocalStored = false
        val expectedNoResults = "No Results"
        val result = formatterInfo.buildCardDescription(artistName, abstract, isLocalStored)
        assertEquals(expectedNoResults, result)
    }
}
