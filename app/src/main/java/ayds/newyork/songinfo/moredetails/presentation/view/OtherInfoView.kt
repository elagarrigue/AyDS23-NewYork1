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
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenter
import ayds.newyork.songinfo.moredetails.injector.DependenciesInjector
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsUIState
import ayds.observer.Observer

class OtherInfoViewActivity(private val formatterInfo: FormatterInfo) : AppCompatActivity() {
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private lateinit var moreDetailsTextPanel: TextView
    private lateinit var imageView: ImageView
    private lateinit var openButton: Button
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader
    private var artistName: String? = null

    private val observer: Observer<MoreDetailsUIState> =
        Observer { value ->
            updateMoreDetailsView(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initModule()
        initProperties()
        loadImage()
        initArtistName()
        subscribeUiState()
        getArtistInfo()
    }

    private fun initModule() {
        DependenciesInjector.init(this)
        moreDetailsPresenter = DependenciesInjector.getPresenter()
    }

    private fun subscribeUiState() {
        moreDetailsPresenter.uiStateObservable.subscribe(observer)
    }

    private fun initProperties() {
        moreDetailsTextPanel = findViewById(R.id.textPanelMoreDetails)
        imageView = findViewById(R.id.imageView)
        openButton = findViewById(R.id.openUrlButton)
    }

    private fun loadImage() {
        runOnUiThread {
            imageLoader.loadImageIntoView(moreDetailsPresenter.uiState.imageURL, imageView)
        }
    }

    private fun initArtistName() {
        artistName = intent.getStringExtra(moreDetailsPresenter.uiState.artistNameExtra)
    }

    private fun getArtistInfo() {
        artistName?.let { name ->
            moreDetailsPresenter.getArtistInfo(name)
        }
    }

    private fun updateMoreDetailsView(uiState: MoreDetailsUIState) {
        updateMoreDetailsTextPanel(uiState.artistName, uiState.abstract, uiState.isLocallyStored)
        updateUrl(uiState.url)
    }

    private fun updateUrl(urlString: String?) {
        openButton.setOnClickListener {
            openURL(urlString)
        }
    }

    private fun openURL(urlString: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        startActivity(intent)
    }

    private fun updateMoreDetailsTextPanel(
        artistName: String,
        abstract: String?,
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

    companion object {
        fun getArtistNameExtra(): String {
            return DependenciesInjector.getPresenter().uiState.artistNameExtra
        }
    }
}