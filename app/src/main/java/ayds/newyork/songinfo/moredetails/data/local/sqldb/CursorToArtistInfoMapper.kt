package ayds.newyork.songinfo.moredetails.data.local.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.moredetails.domain.Source


interface CursorToArtistInfoMapper {
    fun map(cursor: Cursor): List<Card>
}

internal class CursorToArtistInfoMapperImpl : CursorToArtistInfoMapper {
    override fun map(cursor: Cursor): List<Card> {
        val cardList = mutableListOf<Card>()
        var sourceCard: Source = Source.UNKNOWN
        while (cursor.moveToNext()) {
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            val infoUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INFO_URL))
            val sourceLogo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOURCE_URL))
            val source = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOURCE))
            when (source) {
                "LAST_FM" -> sourceCard = Source.LAST_FM
                "WIKIPEDIA" -> sourceCard = Source.WIKIPEDIA
                "NEW_YORK_TIMES" -> sourceCard = Source.NEW_YORK_TIMES
            }
            val card = Card.DataCard(description, infoUrl, sourceCard, sourceLogo)
            cardList.add(card)
        }
        cursor.close()
        return cardList.toList()
    }

}