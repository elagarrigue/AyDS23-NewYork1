package ayds.newyork.songinfo.moredetails.fulllogic.model.data.local.sqldb


const val TABLE_NAME = "artists"
const val COLUMN_ID = "id"
const val COLUMN_NAME = "name"
const val COLUMN_INFO = "info"
const val COLUMN_SOURCE = "source"
const val COLUMN_URL = "url"
const val DATABASE_NAME = "dictionary.db"
const val DATABASE_VERSION = 1
const val CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " +
        "$TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "$COLUMN_NAME TEXT NOT NULL, $COLUMN_INFO TEXT NOT NULL," +
        "$COLUMN_SOURCE INTEGER NOT NULL, " +
        "$COLUMN_URL STRING)"
const val WHERE = "$COLUMN_NAME = ?"
const val SORT_ORDER = "$COLUMN_NAME DESC"

