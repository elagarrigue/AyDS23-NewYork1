package ayds.newyork.songinfo.moredetails.fulllogic.model.data.external.info

import ayds.newyork.songinfo.home.model.repository.external.spotify.tracks.*
import ayds.newyork.songinfo.moredetails.fulllogic.model.domain.ArtistInformation
import com.google.gson.Gson
import com.google.gson.JsonObject

interface NYTtoArtistInfoResolver {
    fun getArtistInfoFromExternalData(serviceData: String?): ArtistInformation?
}

internal class JsonToArtistInfoResolver : NYTtoArtistInfoResolver {

    override fun getArtistInfoFromExternalData(serviceData: String?): ArtistInformation? {
        return try {
            serviceData?.getFirstItem()?.let { item ->
                ArtistInformation(
                    item.getId(),
                    item.getSongName(),
                    item.getArtistName(),
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun String?.getFirstItem(): JsonObject? {
        return null
       /* val jobj = Gson().fromJson(this, JsonObject::class.java)
        val tracks = jobj[TRACKS].asJsonObject
        val items = tracks[ITEMS].asJsonArray
        return items[0].asJsonObject*/
    }
    private fun JsonObject.getId() = ""/*this[ID].asString*/
    private fun JsonObject.getSongName() = "" //this[NAME].asString
    private fun JsonObject.getArtistName(): String {
        return ""
        /*val artist = this[ARTISTS].asJsonArray[0].asJsonObject
        return artist[NAME].asString*/
    }
    /* private fun JsonObject.getAlbumName(): String {
        val album = this[ALBUM].asJsonObject
        return album[NAME].asString
    }
    private fun JsonObject.getReleaseDate(): String {
       /* val album = this[ALBUM].asJsonObject
        return album[RELEASE_DATE].asString*/
    }
    private fun JsonObject.getImageUrl(): String {
        val album = this[ALBUM].asJsonObject
        return album[IMAGES].asJsonArray[1].asJsonObject[URL].asString
    }
    private fun JsonObject.getSpotifyUrl(): String {
        val externalUrl = this[EXTERNAL_URL].asJsonObject
        return externalUrl[SPOTIFY].asString
    }
    private fun JsonObject.getReleaseDatePrecision(): String {
        val album = this[ALBUM].asJsonObject
        return album[RELEASE_DATE_PRECISION].asString
    }*/

}