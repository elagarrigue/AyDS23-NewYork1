package ayds.newyork.songinfo.moredetails.presentation.view

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Method

class FormatterInfoTest {

    private lateinit var formatterInfo: FormatterInfo

    @Before
    fun setUp() {
        formatterInfo = FormatterInfo("John Doe")
    }

    @Test
    fun testBuildArtistInfoAbstract_withNonBlankAbstractAndIsLocalStored_shouldReturnFormattedText() {
        // Arrange
        val artistName = "Bad Bunny"
        val abstract = "This is the Bad Bunny's abstract"
        val isLocalStored = true
        val expectedFormattedText = "<html><div width=400><font face=\"arial\">[*] This is the <b>BAD BUNNY</b>'s abstract</font></div></html>"

        // Act
        val result = formatterInfo.buildArtistInfoAbstract(artistName, abstract, isLocalStored)

        // Assert
        assertEquals(expectedFormattedText, result)
    }

    @Test
    fun testBuildArtistInfoAbstract_withBlankAbstractAndIsLocalStored_shouldReturnNoResults() {
        // Arrange
        val abstract = ""
        val isLocalStored = true
        val expectedNoResults = "No Results"

        // Act
        val result = formatterInfo.buildArtistInfoAbstract(formatterInfo.artistName, abstract, isLocalStored)

        // Assert
        assertEquals(expectedNoResults, result)
    }

    @Test
    fun testBuildArtistInfoAbstract_withNonBlankAbstractAndNotLocalStored_shouldReturnNoResults() {
        // Arrange
        val artistName = "John Doe"
        val abstract = "This is the John Doe's abstract"
        val isLocalStored = false
        val expectedNoResults = "No Results"

        // Act
        val result = formatterInfo.buildArtistInfoAbstract(artistName, abstract, isLocalStored)

        // Assert
        assertEquals(expectedNoResults, result)
    }

    @Test
    fun testReplaceText_shouldReplaceArtistNameInText() {
        // Arrange
        val text = "This is the artist JOHN DOE's abstract"
        val expectedReplacedText = "This is the artist <b>JOHN DOE</b>'s abstract"

        // Act
        val result = invokePrivateMethod(formatterInfo, "replaceText", text) as String

        // Assert
        assertEquals(expectedReplacedText, result)
    }

    @Test
    fun testTextToHtml_shouldConvertTextToHtmlFormat() {
        // Arrange
        val text = "This is the artist's abstract"
        val expectedHtmlFormat = "<html><div width=400><font face=\"arial\">This is the artist's abstract</font></div></html>"

        // Act
        val result = invokePrivateMethod(formatterInfo, "textToHtml", text) as String

        // Assert
        assertEquals(expectedHtmlFormat, result)
    }

    // Método auxiliar para invocar métodos privados utilizando reflexión
    private fun invokePrivateMethod(obj: Any, methodName: String, vararg args: Any?): Any? {
        val method: Method = obj.javaClass.getDeclaredMethod(methodName, *args.map { it?.javaClass ?: Any::class.java }.toTypedArray())
        method.isAccessible = true
        return method.invoke(obj, *args)
    }
}
