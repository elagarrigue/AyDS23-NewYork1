package ayds.newyork.songinfo.moredetails.fulllogic.model.data.local.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.fulllogic.model.domain.ArtistInformation
import java.sql.SQLException

interface CursorToArtistInfoMapper {
    fun map(cursor: Cursor): ArtistInformation?
}

internal class CursorToArtistInfoMapperImpl : CursorToArtistInfoMapper {
    override fun map(cursor: Cursor): ArtistInformation? {
        return try {
            with(cursor) {
                if (cursor.moveToFirst()) {
                    val artist = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                    val info = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INFO))
                    val url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL))
                    ArtistInformation(artist, info, url)
                } else null
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

}