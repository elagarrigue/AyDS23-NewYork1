package ayds.newyork.songinfo.moredetails.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.domain.Card

class CardsAdapter : RecyclerView.Adapter<CardViewHolder>() {

    private var cards: List<Card> = emptyList()

    fun setCards(cards: List<Card>) {
        this.cards = cards
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_card, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position]
        if (card is Card.DataCard)
            holder.bind(card)
    }

    override fun getItemCount(): Int {
        return cards.size
    }
}