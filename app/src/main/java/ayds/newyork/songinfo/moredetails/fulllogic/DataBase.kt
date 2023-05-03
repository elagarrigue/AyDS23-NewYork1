package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DataBase(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(database: SQLiteDatabase) {
        createTable(database)
    }
    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    private fun createTable(database: SQLiteDatabase) {
        val createTableSql = ("$CREATE_TABLE $TABLE_NAME ("
                + "$COLUMN_ID $PRIMARY_KEY "
                + "$COLUMN_NAME $TEXT_NN, "
                + "$COLUMN_INFO $TEXT_NN, "
                + "$COLUMN_SOURCE $INTEGER_NN)")
        database.execSQL(createTableSql)
    }

    companion object {
        private const val DATABASE_NAME = "dictionary.db"
        private const val DATABASE_VERSION = 1
        private const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS"
        private const val PRIMARY_KEY = "INTEGER PRIMARY KEY AUTOINCREMENT, "
        private const val TEXT_NN = "TEXT NOT NULL"
        private const val INTEGER_NN = "INTEGER NOT NULL"
        private const val TABLE_NAME = "artists"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_INFO = "info"
        private const val COLUMN_SOURCE = "source"

        fun saveArtist(dbHelper: DataBase, artist: String?, info: String?) {
            val database = dbHelper.writableDatabase
            database.insert(TABLE_NAME, null, getValues(artist, info))
            database.close()
        }

        private fun getValues(artist: String?, info: String?): ContentValues {
            val values = ContentValues()
            values.put(COLUMN_NAME, artist)
            values.put(COLUMN_INFO, info)
            values.put(COLUMN_SOURCE, 1)
            return values
        }

        fun getInfo(dbHelper: DataBase, artist: String?): String? {
            val database = dbHelper.readableDatabase
            val columns = arrayOf(
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_INFO
            )
            val where = "$COLUMN_NAME = ?"
            val whereArgs = arrayOf(artist)
            val sortOrder = "$COLUMN_NAME DESC"
            val cursor =
                makeQuery(database, TABLE_NAME , columns, where, whereArgs, null, null, sortOrder)
            val items = addItems(cursor)
            cursor.close()
            return if (items.isEmpty()) null else items[0]
        }

        private fun makeQuery(
            database: SQLiteDatabase,
            table: String,
            columns: Array<String>,
            where: String,
            whereArgs: Array<String?>,
            group: String?,
            filter: String?,
            sortOrder: String
        ): Cursor {
            return database.query(
                table,
                columns,
                where,
                whereArgs,
                group,
                filter,
                sortOrder
            )
        }

        private fun addItems(cursor: Cursor): List<String> {
            val items: MutableList<String> = ArrayList()
            while (cursor.moveToNext()) {
                val info = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INFO))
                items.add(info)
            }
            return items
        }
    }
}