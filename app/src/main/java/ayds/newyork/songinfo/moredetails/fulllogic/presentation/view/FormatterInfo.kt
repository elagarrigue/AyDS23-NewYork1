package ayds.newyork.songinfo.moredetails.fulllogic.presentation.view

import java.util.*

class FormatterInfo {

    private var artistName: String = ""

    fun buildArtistInfoAbstract(artistName: String, abstract: String, isLocalStored: Boolean): String {
        this.artistName = artistName
        if (abstract != null && abstract != "") {
            if (isLocalStored) {
                val text = PREFIX.plus(SPACE).plus("${abstract}")
                return getFormattedTextFromAbstract(text!!)
            } else {
                return NO_RESULTS
            }
        } else {
            return NO_RESULTS
        }
    }

    private fun getFormattedTextFromAbstract(abstract: String): String {
        val text = abstract.replace(ENTER_LINE_ESCAPE_SEQ, ENTER_LINE)
        val textFormatted = replaceText(text)
        return textToHtml(textFormatted)
    }

    private fun replaceText(text: String): String {
        val textWithSpaces = text.replace(APOSTROPHE, SPACE)
        val textWithEnterLines = textWithSpaces.replace(ENTER_LINE, HTML_BREAK)
        val termUpperCase = artistName?.uppercase(Locale.getDefault())
        return textWithEnterLines.replace(
            "(?i)$artistName".toRegex(),
            "$HTML_BOLD$termUpperCase$HTML_BOLD_CLOSE"
        )
    }

    private fun textToHtml(text: String): String {
        return StringBuilder()
            .append(HTML_DIV_WIDTH)
            .append(HTML_FONT_FACE)
            .append(text)
            .append(HTML_CLOSE_TAGS)
            .toString()
    }
}