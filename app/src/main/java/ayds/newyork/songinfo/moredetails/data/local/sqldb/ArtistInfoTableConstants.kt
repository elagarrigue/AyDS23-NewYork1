package ayds.newyork.songinfo.moredetails.data.local.sqldb


const val TABLE_NAME = "artists"
const val COLUMN_ID = "id"
const val COLUMN_ARTIST_NAME = "artist_name"
const val COLUMN_DESCRIPTION = "description"
const val COLUMN_INFO_URL = "info_url"
const val COLUMN_SOURCE = "source"
const val COLUMN_SOURCE_URL = "source_url"
const val DATABASE_NAME = "dictionary.db"
const val DATABASE_VERSION = 1
const val CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " +
        "$TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "$COLUMN_ARTIST_NAME TEXT NOT NULL, " +
        "$COLUMN_DESCRIPTION TEXT NOT NULL, $COLUMN_INFO_URL TEXT NOT NULL," +
        "$COLUMN_SOURCE INTEGER NOT NULL, " +
        "$COLUMN_SOURCE_URL TEXT)"
const val WHERE = "$COLUMN_ARTIST_NAME = ?"
const val SORT_ORDER = "$COLUMN_ARTIST_NAME DESC"

