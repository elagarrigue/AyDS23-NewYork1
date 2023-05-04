package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.database.Cursor
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_NAME = "dictionary.db"
private const val DATABASE_VERSION = 1
private const val TABLE_NAME = "artists"
private const val COLUMN_ID = "id"
private const val COLUMN_NAME = "name"
private const val COLUMN_INFO = "info"
private const val COLUMN_SOURCE = "source"
private const val COLUMN_URL = "url"
private const val CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " +
        "$TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "$COLUMN_NAME TEXT NOT NULL, $COLUMN_INFO TEXT NOT NULL," +
        "$COLUMN_SOURCE INTEGER NOT NULL, " +
        "$COLUMN_URL STRING)"
private const val WHERE = "$COLUMN_NAME = ?"
private const val SORT_ORDER = "$COLUMN_NAME DESC"
class DataBase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}


    fun saveArtistInfo(artistInfo: ArtistInfo) {
        this.writableDatabase.insert(TABLE_NAME, null, getValues(artistInfo))
        this.writableDatabase.close()
    }

    fun getInfo(artist: String): ArtistInfo? {
        val columns = arrayOf(
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_INFO,
            COLUMN_URL
        )
        val cursor =
            getCursor(TABLE_NAME, columns, WHERE, arrayOf(artist), null, null, SORT_ORDER)
        val artistInfo = getInfoFromCursor(cursor)
        cursor.close()
        return artistInfo
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
    private fun getInfoFromCursor(cursor: Cursor): ArtistInfo? {
        return try {
            if (cursor.moveToFirst()) {
                val artist = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val info = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INFO))
                val url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL))
                ArtistInfo(artist, info, url)
            } else null
        } catch (err: IllegalArgumentException) {
            err.printStackTrace()
            null
        }
    }
    private fun getValues(artistInfo: ArtistInfo): ContentValues {
        val values = ContentValues()
        values.put(COLUMN_NAME, artistInfo.artistName)
        values.put(COLUMN_INFO, artistInfo.abstract)
        values.put(COLUMN_SOURCE, 1)
        values.put(COLUMN_URL,artistInfo.url)
        return values
    }

}