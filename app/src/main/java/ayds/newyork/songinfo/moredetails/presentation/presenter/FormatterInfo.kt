package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.presentation.view.*
import java.util.*

class FormatterInfo(var artistName: String) {
    fun buildCardDescription(
        description: String,
        artistName: String,
        isLocalStored: Boolean
    ): String {
        val formattedDescription: String
        this.artistName = artistName

        if (description.isNullOrBlank()) {
            formattedDescription = "NO RESULTS"
        } else {
            val text = if (isLocalStored) {
                "$PREFIX $description"
            } else {
                description
            }
            formattedDescription = getFormattedTextFromDescription(text)
        }

        return formattedDescription
    }


    private fun getFormattedTextFromDescription(description: String): String {
        val text = description.replace(ENTER_LINE_ESCAPE_SEQ, ENTER_LINE)
        val textFormatted = replaceText(text)
        return textToHtml(textFormatted)
    }

    private fun replaceText(text: String): String {
        val textWithEnterLines = text.replace(ENTER_LINE, HTML_BREAK)
        val termUpperCase = artistName.uppercase(Locale.getDefault())
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