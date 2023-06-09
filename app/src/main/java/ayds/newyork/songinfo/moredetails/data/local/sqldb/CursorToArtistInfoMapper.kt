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
          while (cursor.moveToNext()) {
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            val infoUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INFO_URL))
            val sourceLogo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOURCE_URL))
            val source = Source.values()[cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SOURCE))]
            val card = Card.DataCard(description, infoUrl, source, sourceLogo)
            cardList.add(card)
        }
        cursor.close()
        return cardList.toList()
    }

}