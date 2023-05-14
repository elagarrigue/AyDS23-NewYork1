package ayds.newyork.songinfo.moredetails.presentation.view

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
import ayds.newyork.songinfo.moredetails.presentation.presenter.Presenter
import ayds.newyork.songinfo.moredetails.injector.DependenciesInjector

interface OtherInfoView {
    fun getArtistNameExtra(): String
}

class OtherInfoViewActivity(private val formatterInfo: FormatterInfo) : AppCompatActivity(),
    OtherInfoView {

    private lateinit var moreDetailsTextPanel: TextView
    private lateinit var imageView: ImageView
    private lateinit var openButton: Button
    private lateinit var presenter: Presenter
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader
    private var artistName: String? = null

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
        DependenciesInjector.init(this)
        presenter = DependenciesInjector.getPresenter()
    }

    private fun initProperties() {
        moreDetailsTextPanel = findViewById(R.id.textPanelMoreDetails)
        imageView = findViewById(R.id.imageView)
        openButton = findViewById(R.id.openUrlButton)
    }

    private fun loadImage() {
        runOnUiThread {
            imageLoader.loadImageIntoView(presenter.uiState.imageURL, imageView)
        }
    }

    private fun initArtistName() {
        artistName = intent.getStringExtra(presenter.uiState.artistNameExtra)
    }

    private fun getArtistInfo() = Thread {
        artistName?.let { name ->
            presenter.getArtistInfo(name)
            updateListeners()
        }
    }.start()

    private fun updateListeners() {
        var url = presenter.uiState.url
        var abstract = presenter.uiState.abstract
        var isLocalStored = presenter.uiState.isLocallyStored
        var artistName = presenter.uiState.artistName

        if (url != null)
            setListener(url)
        if (abstract != null) {
            updateMoreDetailsTextPanel(artistName, abstract, isLocalStored)
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


    private fun updateMoreDetailsTextPanel(
        artistName: String,
        abstract: String,
        isLocalStored: Boolean
    ) {
        runOnUiThread {
            moreDetailsTextPanel.text =
                Html.fromHtml(
                    formatterInfo.buildArtistInfoAbstract(
                        artistName,
                        abstract,
                        isLocalStored
                    )
                )
        }
    }

    override fun getArtistNameExtra(): String {
        return presenter.uiState.artistNameExtra
    }

}