package ayds.newyork.songinfo.moredetails.data.local.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.moredetails.domain.Source


interface CursorToArtistInfoMapper {
    fun map(cursor: Cursor): Card?
}

internal class CursorToArtistInfoMapperImpl : CursorToArtistInfoMapper {
    override fun map(cursor: Cursor): Card? {
        return if (cursor.moveToFirst()) {
            val artist = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            val info = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INFO_URL))
            val sourceLogo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOURCE_URL))
            Card.DataCard(artist, info, Source.UNKNOWN, sourceLogo)
        } else Card.EmptyCard

    }
}