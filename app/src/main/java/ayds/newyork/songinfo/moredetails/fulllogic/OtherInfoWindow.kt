package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.utils.UtilsInjector
import ayds.newyork.songinfo.utils.view.ImageLoader
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

private const val RESPONSE = "response"
private const val DOCS = "docs"
private const val ABSTRACT = "abstract"
private const val URL = "web_url"
private const val NO_RESULTS = "No Results"
private const val HTML_DIV_WIDTH = "<html><div width=400>"
private const val HTML_FONT_FACE = "<font face=\"arial\">"
private const val HTML_CLOSE_TAGS = "</font></div></html>"
private const val APOSTROPHE = "'"
private const val SPACE = " "
private const val ENTER_LINE = "\n"
private const val ENTER_LINE_ESCAPE_SEQ = "\\n"
private const val HTML_BREAK = "<br>"
private const val HTML_BOLD = "<b>"
private const val HTML_BOLD_CLOSE = "</b>"
private const val PREFIX = "[*]"

class OtherInfoWindow : AppCompatActivity() {

    private lateinit var moreDetailsTextPanel: TextView
    private lateinit var imageView: ImageView
    private lateinit var openButton: Button
    private lateinit var dataBase: DataBase
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader
    private var artistName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initProperties()
        initDataBase()
        loadImage()
        initArtistName()
        getArtistInfo()
    }

    private fun initProperties() {
        moreDetailsTextPanel = findViewById(R.id.textPanelMoreDetails)
        imageView = findViewById(R.id.imageView)
        openButton = findViewById(R.id.openUrlButton)
    }

    private fun initDataBase() {
        dataBase = DataBase(this)
    }

    private fun loadImage() {
        runOnUiThread {
            imageLoader.loadImageIntoView(IMAGE_URL, imageView)
        }
    }

    private fun initArtistName() {
        artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
    }

    private fun getArtistInfo() {
        Thread {
            val artistInfo = searchArtistInfo()
            updateArtistInfo(artistInfo)
        }.start()
    }

    private fun updateArtistInfo(artistInfo: ArtistInfo?) {
       /* if (artistInfo?.url != null)
            setListener(artistInfo.url)*/
        if (artistInfo?.abstract != null) {
            updateMoreDetailsTextPanel(artistInfo)
        }
    }

    private fun buildArtistInfoAbstract(artistInfo: ArtistInfo): String? {
        if (artistInfo.isLocallyStored) artistInfo.abstract =
            PREFIX.plus(SPACE).plus("${artistInfo.abstract}")
        return artistInfo.abstract
    }

    private fun searchArtistInfo(): ArtistInfo? {
        var artistInfo = getInfoFromDataBase()
        when {
            artistInfo != null -> {
                markArtistInfoAsLocal(artistInfo)
            }
            else -> {
                artistInfo = getInfoFromNYAPI()
                artistInfo?.let {
                    dataBase.saveArtistInfo(artistInfo)
                }
            }
        }
        return artistInfo
    }

    private fun markArtistInfoAsLocal(artistInfo: ArtistInfo) {
        artistInfo.isLocallyStored = true
    }

    private fun getTextFromAbstract(abstract: String?) =
        if (abstract != null && abstract != "") getFormattedTextFromAbstract(abstract) else NO_RESULTS

    private fun getFormattedTextFromAbstract(abstract: String): String {
        val text = abstract.replace(ENTER_LINE_ESCAPE_SEQ, ENTER_LINE)
        val textFormatted = replaceText(text)
        return textToHtml(textFormatted)
    }

    private fun replaceText(text: String): String {
        val textWithSpaces = text.replace(APOSTROPHE, SPACE)
        val textWithEnterLines = textWithSpaces.replace(ENTER_LINE, HTML_BREAK)
        val termUpperCase = artistName?.uppercase(Locale.getDefault())
        return textWithEnterLines.replace(
            "(?i)$artistName".toRegex(),
            "$HTML_BOLD$termUpperCase$HTML_BOLD_CLOSE"
        )
    }

    private fun getInfoFromDataBase(): ArtistInfo? {
        return if (artistName != null) dataBase.getInfo(artistName!!) else null
    }

    private fun getInfoFromNYAPI(): ArtistInfo? {
        return try {
            val callResponse: Response<String> = newYorkTimesAPI.getArtistInfo(artistName).execute()
            val jobj = Gson().fromJson(callResponse.body(), JsonObject::class.java)
            return jsonToArtistInfo(jobj)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun jsonToArtistInfo(jobj: JsonObject?): ArtistInfo? {
        if (jobj == null)
            return null
        val response = jobj.get(RESPONSE).asJsonObject
        val docs = response[DOCS].asJsonArray
        val abstract = if (docs.size() == 0) getTextFromAbstract(null) else getTextFromAbstract(
            docs.get(0).asJsonObject.get(ABSTRACT).asString
        )
        val url = if (docs.size() == 0) null
                  else docs.get(0).asJsonObject.get(URL).asString
        return artistName?.let {
            ArtistInfo(
                it, abstract, url
            )
        }
    }

  /*  private fun setListener(urlString: String) {
        openButton.setOnClickListener {
            openURL(urlString)
        }
    }*/

    private fun openURL(urlString: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        startActivity(intent)
    }


    private fun updateMoreDetailsTextPanel(artistInfo: ArtistInfo) {
        runOnUiThread {
            moreDetailsTextPanel.text = Html.fromHtml(buildArtistInfoAbstract(artistInfo))
        }
    }

    private fun textToHtml(text: String): String {
        return StringBuilder()
            .append(HTML_DIV_WIDTH)
            .append(HTML_FONT_FACE)
            .append(text)
            .append(HTML_CLOSE_TAGS)
            .toString()
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        const val IMAGE_URL =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
        private const val NYTIMES_URL = "https://api.nytimes.com/svc/search/v2/"
        private val newYorkTimesRetrofit = Retrofit.Builder()
            .baseUrl(NYTIMES_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        private val newYorkTimesAPI: NYTimesAPI = newYorkTimesRetrofit.create(NYTimesAPI::class.java)
    }
}
