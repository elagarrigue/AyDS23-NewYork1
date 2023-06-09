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

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(database)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    override fun saveData(data: List<Card>, artistName: String) {
        val database = this.writableDatabase
        database.use { database ->
            for (card in data) {
                if (card is Card.DataCard) {
                    database.insert(TABLE_NAME, null, getValues(card, artistName))
                }
            }
        }
    }

    override fun getData(data: String): List<Card> {
        val columns = arrayOf(
            COLUMN_ID,
            COLUMN_ARTIST_NAME,
            COLUMN_DESCRIPTION,
            COLUMN_INFO_URL,
            COLUMN_SOURCE,
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

    private fun getValues(data: Card.DataCard, artistName: String): ContentValues =
        ContentValues().apply {
            put(COLUMN_ARTIST_NAME, artistName)
            put(COLUMN_DESCRIPTION, data.description)
            put(COLUMN_INFO_URL, data.infoUrl)
            put(COLUMN_SOURCE, data.source?.ordinal)
            put(COLUMN_SOURCE_URL, data.sourceLogoUrl)
        }


}