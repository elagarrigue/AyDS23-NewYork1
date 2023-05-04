package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.fulllogic.DataBase.Companion.getInfo
import ayds.newyork.songinfo.moredetails.fulllogic.DataBase.Companion.saveArtist
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.*

class OtherInfoWindow : AppCompatActivity() {
    private var textPanelInfo: TextView? = null
    var dataBase = DataBase(this)
    private val imageUrl =
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

    private fun open(artist: String?) {
        getArtistInfo(artist)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textView()
        open(intent.getStringExtra(ARTIST_NAME_EXTRA))
    }

    private fun textView() {
        setContentView(R.layout.activity_other_info)
        textPanelInfo = findViewById(R.id.textPanelInfo)
    }

    private fun getArtistInfo(artistName: String?) {
        Thread {
            runOnUiThread {
                loadAndSet(artistName)
            }
        }.start()
    }

    private fun loadAndSet(artistName: String?) {
        loadImage()
        setTextOnView(getArtistText(artistName))
    }

    private fun loadImage() {
        val imageView = findViewById<ImageView>(R.id.imageView)
        Picasso.get().load(imageUrl).into(imageView)
    }

    private fun setTextOnView(text: String?) {
        textPanelInfo?.text = Html.fromHtml(text)
    }

    private fun getArtistText(artistName: String?): String? {
        return textFormat(artistInfo(artistName), artistName)
    }

    private fun artistInfo(artistName: String?): String? {
        return getInfo(dataBase, artistName)
    }

    private fun textFormat(text: String?, artistName: String?): String? {
        if (text != null) {
            return "[*]$text"
        } else {
            val response = getResponse(artistName, createNYTimesAPI())
            val abstractElement = getAbstractElement(response)
            val urlElement = getURLElement(response)
            return if (abstractElement == null) {
                "No Results"
            } else {
                val htmlText = formatFinal(abstractElement, artistName)
                saveArtist(dataBase, artistName, htmlText)
                setListener(urlElement?.asString)
                htmlText
            }
        }
    }

    private fun formatFinal(abstractElement: JsonElement?, artistName: String?): String? {
        val formattedText = abstractElement?.asString?.replace("\\n", "\n")
        return textToHtml(formattedText, artistName)
    }

    private fun getResponse(artistName: String?, NYTimesAPI: NYTimesAPI): JsonArray? {
        return try {
            jsonToArray(NYTimesAPI.getArtistInfo(artistName).execute())

        } catch (e1: IOException) {
            e1.printStackTrace()
            null
        }
    }

    private fun getAbstractElement(response: JsonArray?): JsonElement? {
        return response?.get(0)?.asJsonObject?.get("abstract")
    }

    private fun getURLElement(response: JsonArray?): JsonElement? {
        return response?.get(0)?.asJsonObject?.get("web_url")
    }

    private fun jsonToArray(callResponse: Response<String>): JsonArray {
        val gson = Gson()
        val jObj = gson.fromJson(callResponse.body(), JsonObject::class.java)
        val responseObj = jObj.get("response").asJsonObject
        return responseObj.getAsJsonArray("docs")
    }

    private fun setListener(urlString: String?) {
        findViewById<View>(R.id.openUrlButton).setOnClickListener { v: View? ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            startActivity(intent)
        }
    }

    private fun createNYTimesAPI(): NYTimesAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(NYTimesAPI::class.java)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        private const val BASE_URL = "https://api.nytimes.com/svc/search/v2/"
        private const val DIV_WIDTH = "<html><div width=400>"
        private const val FONT_FACE = "<font face=\"arial\">"
        private const val CLOSE_TAG = "</font></div></html>"
        private const val ENTER_LINE = "\n"
        private const val BR = "<br>"
        private const val BOLD = "<b>"
        private const val CLOSE_BOLD = "</b>"

        fun textToHtml(text: String?, term: String?): String? {
            val builder = StringBuilder()
            builder.append(DIV_WIDTH)
            builder.append(FONT_FACE)
            builder.append(textWithBold(text, term))
            builder.append(CLOSE_TAG)
            return builder.toString()
        }

        private fun textWithBold(text: String?, term: String?): String? =
            text?.replace("'", " ")?.replace(ENTER_LINE, BR)?.replace(
                "(?i)$term".toRegex(),
                BOLD + term?.uppercase(Locale.getDefault()) + CLOSE_BOLD
            )
    }

}