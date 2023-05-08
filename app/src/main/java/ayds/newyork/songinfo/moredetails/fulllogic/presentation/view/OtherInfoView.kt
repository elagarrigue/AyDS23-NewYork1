package ayds.newyork.songinfo.moredetails.fulllogic.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.fulllogic.model.domain.ArtistInformation
import ayds.newyork.songinfo.moredetails.fulllogic.presentation.MoreDetailsPresentationInjector
import ayds.newyork.songinfo.moredetails.fulllogic.presentation.MoreDetailsUIEvent
import ayds.newyork.songinfo.moredetails.fulllogic.presentation.MoreDetailsUIState
import ayds.newyork.songinfo.utils.UtilsInjector
import ayds.newyork.songinfo.utils.view.ImageLoader
import ayds.newyork.songinfo.moredetails.fulllogic.presentation.presenter.Presenter
import ayds.observer.Observable
import ayds.observer.Subject

const val ARTIST_NAME_EXTRA = "artistName"
const val IMAGE_URL =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

interface OtherInfoView {
    val uiEventObservable: Observable<MoreDetailsUIEvent>
    val uiState: MoreDetailsUIState

    companion object {
        fun getArtistNameExtra(): String {
            return ARTIST_NAME_EXTRA
        }
    }
}

class OtherInfoViewActivity(private val formatterInfo: FormatterInfo) : AppCompatActivity(),
    OtherInfoView {

    private val onActionSubject = Subject<MoreDetailsUIEvent>()
    private lateinit var moreDetailsTextPanel: TextView
    private lateinit var imageView: ImageView
    private lateinit var openButton: Button
    private lateinit var presenter: Presenter
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader
    private var artistName: String? = null

    override val uiEventObservable: Observable<MoreDetailsUIEvent> = onActionSubject
    override var uiState: MoreDetailsUIState = MoreDetailsUIState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initModule()
        initProperties()
        loadImage()
        initArtistName()
        getArtistInfo()
    }

    private fun initModule() {
        MoreDetailsPresentationInjector.initView(this)
        presenter = MoreDetailsPresentationInjector.getPresenter()
    }

    private fun initProperties() {
        moreDetailsTextPanel = findViewById(R.id.textPanelMoreDetails)
        imageView = findViewById(R.id.imageView)
        openButton = findViewById(R.id.openUrlButton)
    }

    private fun loadImage() {
        runOnUiThread {
            imageLoader.loadImageIntoView(IMAGE_URL, imageView)
        }
    }

    private fun initArtistName() {
        artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
    }

    private fun getArtistInfo() = Thread {
        val artistInfo = presenter.getArtistInfo(artistName)
        updateListeners(artistInfo)
    }.start()

    private fun updateListeners(artistInfo: ArtistInformation?) {
        if (artistInfo?.url != null)
            setListener(artistInfo.url)
        if (artistInfo?.abstract != null) {
            updateMoreDetailsTextPanel(artistInfo)
        }
    }

    private fun setListener(urlString: String) {
        openButton.setOnClickListener {
            openURL(urlString)
        }
    }

    private fun openURL(urlString: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        startActivity(intent)
    }


    private fun updateMoreDetailsTextPanel(artistInfo: ArtistInformation) {
        runOnUiThread {
            moreDetailsTextPanel.text =
                Html.fromHtml(formatterInfo.buildArtistInfoAbstract(artistInfo))
        }
    }

}