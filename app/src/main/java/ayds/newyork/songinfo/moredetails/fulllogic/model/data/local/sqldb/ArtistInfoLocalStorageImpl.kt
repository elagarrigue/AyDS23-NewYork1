package ayds.newyork.songinfo.moredetails.fulllogic.model.data.local.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.newyork.songinfo.moredetails.fulllogic.model.domain.ArtistInformation
import ayds.newyork.songinfo.moredetails.fulllogic.model.data.local.ArtistInfoLocalStorage


class ArtistInfoLocalStorageImpl(
    context: Context,
    private val cursorToArtistInfoMapper: CursorToArtistInfoMapper,
) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION), ArtistInfoLocalStorage {

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    override fun saveArtistInfo(artistInfo: ArtistInformation.ArtistInformationData) {
        this.writableDatabase.insert(TABLE_NAME, null, getValues(artistInfo))
        this.writableDatabase.close()
    }

    override fun getArtistInfo(artist: String): ArtistInformation? {
        val columns = arrayOf(
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_INFO,
            COLUMN_URL
        )
        val cursor =
            getCursor(TABLE_NAME, columns, WHERE, arrayOf(artist), null, null, SORT_ORDER)
        return cursorToArtistInfoMapper.map(cursor)
    }

    private fun getCursor(
        table: String,
        columns: Array<String>,
        where: String,
        whereArgs: Array<String>,
        group: String?,
        filter: String?,
        sortOrder: String
    ): Cursor {
        return this.readableDatabase.query(
            table,
            columns,
            where,
            whereArgs,
            group,
            filter,
            sortOrder
        )
    }

    private fun getValues(artistInfo: ArtistInformation.ArtistInformationData): ContentValues {
        val values = ContentValues()
        values.put(COLUMN_NAME, artistInfo.artistName)
        values.put(COLUMN_INFO, artistInfo.abstract)
        values.put(COLUMN_SOURCE, 1)
        values.put(COLUMN_URL, artistInfo.url)
        return values
    }

}