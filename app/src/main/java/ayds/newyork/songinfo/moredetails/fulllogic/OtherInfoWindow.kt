package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
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
    private var textPane2: TextView? = null
    private var dataBase: DataBase? = null
    private val imageUrl =
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

    private fun open(artist: String?) {
        dataBase = DataBase(this)
        saveArtist(dataBase!!, "test", "sarasa")
        Log.e("TAG", "" + getInfo(dataBase!!, "test"))
        Log.e("TAG", "" + getInfo(dataBase!!, "nada"))
        getArtistInfo(artist)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        textPane2 = findViewById(R.id.textPane2)
        open(intent.getStringExtra("artistName"))
    }

    private fun saveArtist(artist: String?, info: String?) {
        saveArtist(dataBase!!, artist, info)
    }

    private fun getArtistInfo(artistName: String?) {
        val NYTimesAPI = createNYTimesAPI()
        Log.e("TAG", "artistName $artistName")
        Thread {
            var text = getInfo(dataBase!!, artistName!!)
            if (text != null) { // exists in db
                text = "[*]$text"
            } else { // get from service
                try {
                    val callResponse =
                        NYTimesAPI.getArtistInfo(artistName).execute()
                    Log.e("TAG", "JSON " + callResponse.body())
                    val gson = Gson()
                    val jObj =
                        gson.fromJson(callResponse.body(), JsonObject::class.java)
                    val responseObj = jObj["response"].asJsonObject
                    val docsElement =
                        responseObj["docs"].asJsonArray[0].asJsonObject["abstract"]
                    val url =
                        responseObj["docs"].asJsonArray[0].asJsonObject["web_url"]
                    if (docsElement == null) {
                        text = "No Results"
                    } else {
                        text = docsElement.asString.replace("\\n", "\n")
                        text =
                            textToHtml(
                                text,
                                artistName
                            )
                        saveArtist(artistName, text)
                    }
                    findViewByID(url.asString)
                } catch (e1: IOException) {
                    Log.e("TAG", "Error $e1")
                    e1.printStackTrace()
                }
            }
            Log.e("TAG", "Get Image from $imageUrl")
            val finalText = text
            runOnUiThread {
                Picasso.get().load(imageUrl)
                    .into(findViewById<View>(R.id.imageView) as ImageView)
                textPane2!!.text = Html.fromHtml(finalText)
            }
        }.start()
    }

    private fun findViewByID(urlString: String) {
        findViewById<View>(R.id.openUrlButton).setOnClickListener { v: View? ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            startActivity(intent)
        }
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"

        fun textToHtml(text: String, term: String?): String {
            val builder = StringBuilder()
            builder.append("<html><div width=400>")
            builder.append("<font face=\"arial\">")
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace(
                    "(?i)$term".toRegex(),
                    "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
                )
            builder.append(textWithBold)
            builder.append("</font></div></html>")
            return builder.toString()
        }
    }

    private fun createNYTimesAPI(): NYTimesAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/svc/search/v2/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(NYTimesAPI::class.java)
    }
}