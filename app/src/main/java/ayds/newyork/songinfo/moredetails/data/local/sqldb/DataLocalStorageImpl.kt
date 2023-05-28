package ayds.newyork.songinfo.moredetails.data.local.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.moredetails.data.local.DataLocalStorage
import ayds.newyork.songinfo.moredetails.domain.Source


class DataLocalStorageImpl(
    context: Context,
    private val cursorToArtistInfoMapper: CursorToArtistInfoMapper,
) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION), DataLocalStorage {

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    override fun saveData(data: Card.DataCard) {
        this.writableDatabase.insert(TABLE_NAME, null, getValues(data))
        this.writableDatabase.close()
    }

    override fun getData(data: String): Card? {
        val columns = arrayOf(
            COLUMN_ID,
            COLUMN_DESCRIPTION,
            COLUMN_INFO_URL,
            COLUMN_SOURCE_URL
        )
        val cursor =
            getCursor(TABLE_NAME, columns, WHERE, arrayOf(data), null, null, SORT_ORDER)
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

    private fun getValues(data: Card.DataCard): ContentValues =
        ContentValues().apply {
            put(COLUMN_DESCRIPTION, data.description)
            put(COLUMN_INFO_URL, data.infoUrl)
            put(COLUMN_SOURCE, getSourceValue(data.source))
            put(COLUMN_SOURCE_URL, data.sourceLogoUrl)
        }

    private fun getSourceValue(source: Source): String {
        return when (source) {
            Source.LAST_FM -> "LastFM"
            Source.WIKIPEDIA -> "Wikipedia"
            Source.NEW_YORK_TIMES -> "New York Times"
            Source.UNKNOWN -> "Unknown"
        }
    }

}