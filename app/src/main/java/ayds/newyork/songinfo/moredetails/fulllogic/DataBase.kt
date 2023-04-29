package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBase(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(database: SQLiteDatabase) {
        createTable(database)
        Log.i(this.javaClass.simpleName, "Database created.")
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    private fun createTable(database: SQLiteDatabase) {
        val createTableSql = ("CREATE TABLE IF NOT EXISTS artists ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "info TEXT NOT NULL, "
                + "source INTEGER NOT NULL)")
        database.execSQL(createTableSql)
    }

    companion object {
        private const val DATABASE_NAME = "dictionary.db"
        private const val DATABASE_VERSION = 1
        @JvmStatic
        fun saveArtist(dbHelper: DataBase, artist: String?, info: String?) {
            val database = dbHelper.writableDatabase
            database.insert("artists", null, getValues(artist, info))
            database.close()
        }

        private fun getValues(artist: String?, info: String?): ContentValues {
            val values = ContentValues()
            values.put("name", artist)
            values.put("info", info)
            values.put("source", 1)
            return values
        }

        @JvmStatic
        fun getInfo(dbHelper: DataBase, artist: String): String? {
            val database = dbHelper.readableDatabase
            val columns = arrayOf(
                "id",
                "name",
                "info"
            )
            val where = "name  = ?"
            val whereArgs = arrayOf(artist)
            val sortOrder = "name DESC"
            val cursor =
                makeQuery(database, "artists", columns, where, whereArgs, null, null, sortOrder)
            val items = addItems(cursor)
            cursor.close()
            return if (items.isEmpty()) null else items[0]
        }

        private fun makeQuery(
            database: SQLiteDatabase,
            table: String,
            columns: Array<String>,
            where: String,
            whereArgs: Array<String>,
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
                val info = cursor.getString(cursor.getColumnIndexOrThrow("info"))
                items.add(info)
            }
            return items
        }
    }
}