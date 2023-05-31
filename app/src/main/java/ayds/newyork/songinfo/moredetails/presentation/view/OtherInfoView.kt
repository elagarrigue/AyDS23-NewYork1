package ayds.newyork.songinfo.moredetails.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.utils.UtilsInjector
import ayds.newyork.songinfo.utils.view.ImageLoader
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenter
import ayds.newyork.songinfo.moredetails.injector.MoreDetailsDependenciesInjector
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsUIState
import ayds.observer.Observer
import com.squareup.picasso.Picasso

class OtherInfoViewActivity(private val formatterInfo: FormatterInfo) : AppCompatActivity() {

    private lateinit var cardsRecyclerView: RecyclerView
    private lateinit var cardsAdapter: CardsAdapter

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
        MoreDetailsDependenciesInjector.init(this)
        moreDetailsPresenter = MoreDetailsDependenciesInjector.getPresenter()
    }

    private fun subscribeUiState() {
        moreDetailsPresenter.uiStateObservable.subscribe(observer)
    }

    private fun initProperties() {
        setContentView(R.layout.activity_other_info)
        cardsRecyclerView = findViewById(R.id.cardsRecyclerView)
        cardsAdapter = CardsAdapter()
        cardsRecyclerView.layoutManager = LinearLayoutManager(this)
        cardsRecyclerView.adapter = cardsAdapter


        moreDetailsTextPanel = findViewById(R.id.description)
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

    /*companion object {
        fun getArtistNameExtra(): String {
            return MoreDetailsDependenciesInjector.getPresenter().uiState.artistNameExtra
        }
    }*/

    private class CardsAdapter : RecyclerView.Adapter<CardViewHolder>() {

        private var cards: List<Card> = emptyList()

        fun setCards(cards: List<Card>) {
            this.cards = cards
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_card, parent, false)
            return CardViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
            val card = cards[position]
            if(card is ArtistCard)
                holder.bind(card)
        }

        override fun getItemCount(): Int {
            return cards.size
        }
    }

    private class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val sourceLabelTextView: TextView = itemView.findViewById(R.id.sourceLabelTextView)
        private val sourceTextView: TextView = itemView.findViewById(R.id.sourceTextView)
        private val description: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val openUrlButton: Button = itemView.findViewById(R.id.openUrlButton)

        fun bind(card: Card) {
            Picasso.get().load(card.sourceLogoUrl).into(imageView)
            sourceLabelTextView.text = "Source: "
            description.text = card.description
            sourceTextView.text = card.source.toString()
            openUrlButton.setOnClickListener {
                openExternalUrl(card.infoUrl)
            }
        }

        private fun openExternalUrl(url: String?) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            itemView.context.startActivity(intent)
        }
    }

}