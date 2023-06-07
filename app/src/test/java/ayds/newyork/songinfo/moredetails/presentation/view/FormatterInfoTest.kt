import ayds.newyork.songinfo.moredetails.presentation.presenter.FormatterInfo
import ayds.newyork.songinfo.moredetails.presentation.view.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FormatterInfoTest {
    private lateinit var formatterInfo: FormatterInfo
    @Before
    fun setUp() {
        formatterInfo = FormatterInfo("Artist Name")
    }

    @Test
    fun `buildCardDescription should return NO_RESULTS when description is blank or isLocalStored is false`() {

        val description = "Description"
        val isLocalStored = false

        val result = formatterInfo.buildCardDescription(description, "Artist Name", isLocalStored)

        assertEquals(NO_RESULTS, result)
    }

    @Test
    fun `buildCardDescription should return formatted description when description is not blank and isLocalStored is true`() {

        val description = "Description"
        val isLocalStored = true

        val expectedFormattedDescription = "$HTML_DIV_WIDTH$HTML_FONT_FACE$PREFIX Description $HTML_CLOSE_TAGS"

        val result = formatterInfo.buildCardDescription(description, "Artist Name", isLocalStored)

        assertEquals(expectedFormattedDescription, result)
    }

    @Test
    fun `buildCardDescription should replace artist name and format the text`() {

        val description = "The artist name is artist name."
        val isLocalStored = true
        val ARTIST_NAME = "ARTIST NAME"
        val expectedFormattedDescription =
            "$HTML_DIV_WIDTH$HTML_FONT_FACE$PREFIX The $HTML_BOLD$ARTIST_NAME$HTML_BOLD_CLOSE is $HTML_BOLD$ARTIST_NAME$HTML_BOLD_CLOSE.$HTML_CLOSE_TAGS"

        val result = formatterInfo.buildCardDescription(description, "ARTIST NAME", isLocalStored)

        assertEquals(expectedFormattedDescription, result)
    }
}


