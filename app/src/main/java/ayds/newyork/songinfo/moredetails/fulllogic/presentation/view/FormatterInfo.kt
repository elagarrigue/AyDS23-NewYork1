package ayds.newyork.songinfo.moredetails.fulllogic.presentation.view

import ayds.newyork.songinfo.moredetails.fulllogic.model.domain.ArtistInformation
import java.util.*

class FormatterInfo {

    private var artistName: String = ""

    fun buildArtistInfoAbstract(artistInfo: ArtistInformation): String {
        artistName = artistInfo.artistName
        if (artistInfo.abstract != null && artistInfo.abstract != "") {
            if (artistInfo.isLocallyStored) {
                artistInfo.abstract = PREFIX.plus(SPACE).plus("${artistInfo.abstract}")
                return getFormattedTextFromAbstract(artistInfo.abstract!!)
            } else return NO_RESULTS
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