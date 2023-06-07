package ayds.newyork.songinfo.moredetails.presentation.view


import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.domain.Card
import ayds.newyork.songinfo.utils.UtilsInjector
import ayds.newyork.songinfo.utils.navigation.NavigationUtils
import com.squareup.picasso.Picasso

class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val source: TextView = itemView.findViewById(R.id.source)
    private val description: TextView = itemView.findViewById(R.id.description)
    private val openUrlButton: Button = itemView.findViewById(R.id.openUrlButton)
    private val navigationUtils: NavigationUtils = UtilsInjector.navigationUtils
    fun bind(card: Card) {
        if (card is Card.DataCard) {
            Picasso.get().load(card.sourceLogoUrl).into(imageView)
            source.text = "Source: " + card.source.toString()
            description.text = HtmlCompat.fromHtml(card.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
            openUrlButton.setOnClickListener {
                navigationUtils.openExternalUrl(itemView.context ,card.infoUrl)
            }
        }
    }

}