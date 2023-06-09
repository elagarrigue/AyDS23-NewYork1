package ayds.newyork.songinfo.moredetails.data.local

import android.database.sqlite.SQLiteDatabase
import ayds.newyork.songinfo.moredetails.domain.Card

interface DataLocalStorage {

    fun onCreate(database: SQLiteDatabase)

    fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int)

    fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)

    fun saveData(data: List<Card>, artistName: String)

    fun getData(data: String): List<Card>
}