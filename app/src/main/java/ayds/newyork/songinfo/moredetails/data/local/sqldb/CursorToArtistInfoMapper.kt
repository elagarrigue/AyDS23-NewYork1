package ayds.newyork.songinfo.moredetails.data.local.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.domain.ArtistInformation


interface CursorToArtistInfoMapper {
    fun map(cursor: Cursor): ArtistInformation?
}

internal class CursorToArtistInfoMapperImpl : CursorToArtistInfoMapper {
    override fun map(cursor: Cursor): ArtistInformation? {
        return if (cursor.moveToFirst()) {
            val artist = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val info = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INFO))
            val url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL))
            ArtistInformation.ArtistInformationData(artist, info, url)
        } else ArtistInformation.ArtistInformationEmpty

    }
}