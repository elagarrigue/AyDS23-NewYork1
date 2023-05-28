package ayds.newyork.songinfo.moredetails.data.local.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.domain.Card


interface CursorToArtistInfoMapper {
    fun map(cursor: Cursor): Card?
}

internal class CursorToArtistInfoMapperImpl : CursorToArtistInfoMapper {
    override fun map(cursor: Cursor): Card? {
        return if (cursor.moveToFirst()) {
            val artist = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val info = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INFO_URL))
            val url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOURCE_URL))
            Card.CardData(artist, info, url)
        } else Card.CardEmpty

    }
}