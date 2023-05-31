package ayds.newyork.songinfo.moredetails.presentation.view

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.domain.Card
import com.squareup.picasso.Picasso

class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val source: TextView = itemView.findViewById(R.id.source)
    private val sourceURL: TextView = itemView.findViewById(R.id.sourceURL)
    private val description: TextView = itemView.findViewById(R.id.description)
    private val openUrlButton: Button = itemView.findViewById(R.id.openUrlButton)

    fun bind(card: Card) {
        if (card is Card.DataCard) {
            Picasso.get().load(card.sourceLogoUrl).into(imageView)
            source.text = "Source: "
            description.text = card.description
            sourceURL.text = card.source.toString()
            openUrlButton.setOnClickListener {
                openExternalUrl(card.infoUrl)
            }
        }
    }

    private fun openExternalUrl(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        itemView.context.startActivity(intent)
    }
}