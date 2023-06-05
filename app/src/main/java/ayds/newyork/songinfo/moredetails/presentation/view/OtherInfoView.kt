package ayds.newyork.songinfo.moredetails.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenter
import ayds.newyork.songinfo.moredetails.injector.MoreDetailsDependenciesInjector
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsUIState
import ayds.observer.Observer

private var artistName: String? = null
class OtherInfoViewActivity() : AppCompatActivity() {

    private lateinit var cardsRecyclerView: RecyclerView
    private lateinit var cardsAdapter: CardsAdapter
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter

    private val observer: Observer<MoreDetailsUIState> =
        Observer { value ->
            updateMoreDetailsView(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initModule()
        initProperties()
        initArtistName()
        subscribeUiState()
        getDataInfo()
    }

    private fun initModule() {
        MoreDetailsDependenciesInjector.init(this)
        moreDetailsPresenter = MoreDetailsDependenciesInjector.getPresenter()
    }

    private fun subscribeUiState() {
        moreDetailsPresenter.uiStateObservable.subscribe(observer)
    }

    private fun initProperties() {
        cardsRecyclerView = findViewById(R.id.cardsRecyclerView)
        cardsAdapter = CardsAdapter()
        cardsRecyclerView.layoutManager = LinearLayoutManager(this)
        cardsRecyclerView.adapter = cardsAdapter
    }

    private fun initArtistName() {
        artistName = intent.getStringExtra(artistName)
    }

    private fun getDataInfo() {
        artistName?.let { name ->
            moreDetailsPresenter.getDataArtist(name)
        }
      }

    private fun updateMoreDetailsView(uiState: MoreDetailsUIState) {
        runOnUiThread {
            cardsAdapter.setCards(uiState.dataCards)
        }
    }

    companion object {
        public fun getArtistNameExtra(): String? {
            return artistName
        }
    }
}