package ayds.newyork.songinfo.moredetails.fulllogic.model.data.external.info

import ayds.newyork.songinfo.moredetails.fulllogic.model.domain.ArtistInformation
import com.google.gson.Gson
import com.google.gson.JsonObject

interface NYTtoArtistInfoResolver {
    fun getArtistInfoFromExternalData(name: String, serviceData: String?): ArtistInformation?
}

internal class JsonToArtistInfoResolver : NYTtoArtistInfoResolver {

    override fun getArtistInfoFromExternalData(
        name: String,
        serviceData: String?
    ): ArtistInformation? {
        return try {
            serviceData?.getFirstItem()?.let { item ->
                ArtistInformation(
                    name,
                    item.getAbstract(),
                    item.getURL()
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun String?.getFirstItem(): JsonObject? {
        //return null
        val jobj = Gson().fromJson(this, JsonObject::class.java)
        val docs = jobj[DOCS].asJsonArray
        val items = docs[0].asJsonArray
        return items[0].asJsonObject
    }

    private fun JsonObject.getAbstract() = this[ABSTRACT].asString
    private fun JsonObject.getURL() = this[URL].asString


}