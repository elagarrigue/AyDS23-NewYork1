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
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
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
        setContentView(R.layout.activity_other_info)
        textPanelInfo = findViewById(R.id.textPanelInfo)
        open(intent.getStringExtra(ARTIST_NAME_EXTRA))
    }

   private fun getArtistInfo(artistName: String?) {
        val NYTimesAPI = createNYTimesAPI()
        Thread {
            val text = getArtistText(artistName, NYTimesAPI)
            runOnUiThread {  //cambiar
                val imageView = findViewById<ImageView>(R.id.imageView)
                Picasso.get().load(imageUrl).into(imageView)
                textPanelInfo?.text = Html.fromHtml(text)
            }
        }.start()
    }
    private fun getArtistText(artistName: String?, NYTimesAPI: NYTimesAPI): String {
        val text = getInfo(dataBase, artistName)
        if (text != null) {
            return "[*]$text"
        } else {
            val response = getResponse(artistName, NYTimesAPI)
            val docsElement =
                response?.getAsJsonArray("docs")?.get(0)?.asJsonObject?.get("abstract")
            val url = response?.getAsJsonArray("docs")?.get(0)?.asJsonObject?.get("web_url")
            return if (docsElement == null) {
                "No Results"
            } else {
                val formattedText = docsElement.asString.replace("\\n", "\n")
                val htmlText = textToHtml(formattedText, artistName)
                saveArtist(dataBase,artistName, htmlText)
                setListener(url?.asString)
                htmlText
            }
        }
    }
    private fun getResponse(artistName: String?, NYTimesAPI: NYTimesAPI): JsonObject? {
        try {
            val callResponse = NYTimesAPI.getArtistInfo(artistName).execute()
            val gson = Gson()
            val jObj = gson.fromJson(callResponse.body(), JsonObject::class.java)
            val responseObj = jObj.get("response").asJsonObject
            val docsArray = responseObj.getAsJsonArray("docs")
            if (docsArray == null || docsArray.size() == 0) {
                return null
            }

            return responseObj
        } catch (e1: IOException) {
            e1.printStackTrace()
            return null
        }
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
            .baseUrl("https://api.nytimes.com/svc/search/v2/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(NYTimesAPI::class.java)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        private const val DIV_WIDTH = "<html><div width=400>"
        private const val FONT_FACE = "<font face=\"arial\">"
        private const val CLOSE_TAG = "</font></div></html>"
        private const val ENTER_LINE = "\n"
        private const val BR = "<br>"
        private const val BOLD= "<b>"
        private const val CLOSE_BOLD = "</b>"

        fun textToHtml(text: String, term: String?): String {
            val builder = StringBuilder()
            builder.append(DIV_WIDTH)
            builder.append(FONT_FACE)
            // Pasar a funcion
            val textWithBold = text
                .replace("'", " ")
                .replace(ENTER_LINE, BR)
                .replace(
                    "(?i)$term".toRegex(),
                    BOLD + term!!.uppercase(Locale.getDefault()) + CLOSE_BOLD
                )
            builder.append(textWithBold)
            builder.append(CLOSE_TAG)
            return builder.toString()
        }
    }


}