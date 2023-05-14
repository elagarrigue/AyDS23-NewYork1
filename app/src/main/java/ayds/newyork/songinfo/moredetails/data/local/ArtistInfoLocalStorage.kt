package ayds.newyork.songinfo.moredetails.data.local

import android.database.sqlite.SQLiteDatabase
import ayds.newyork.songinfo.moredetails.domain.ArtistInformation

interface ArtistInfoLocalStorage {

    fun onCreate(database: SQLiteDatabase)

    fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int)

    fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)

    fun saveArtistInfo(artistInfo: ArtistInformation.ArtistInformationData)

    fun getArtistInfo(artist: String): ArtistInformation?
}