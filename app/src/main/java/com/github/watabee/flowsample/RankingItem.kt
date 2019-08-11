package com.github.watabee.flowsample

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import java.util.Locale

data class RankingItemState(
    val itemCode: String,
    val imageUrl: String?,
    val itemName: String,
    val price: Int,
    val shopName: String,
    val isFavorite: Boolean
)

class RankingItem(
    private val state: RankingItemState,
    private val onFavoriteButtonClicked: (state: RankingItemState) -> Unit
) : Item<RankingViewHolder>(state.itemCode.hashCode().toLong()) {
    override fun getLayout(): Int = R.layout.list_item_ranking

    override fun createViewHolder(itemView: View): RankingViewHolder = RankingViewHolder(itemView)

    override fun bind(viewHolder: RankingViewHolder, position: Int) {
        viewHolder.bind(state)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is RankingItem) {
            return false
        }
        return state == other.state
    }

    override fun hashCode(): Int = state.hashCode()

    fun onFavoriteButtonClicked() = onFavoriteButtonClicked(state)
}

class RankingViewHolder(itemView: View) : ViewHolder(itemView) {

    private val imageView: ImageView = itemView.findViewById(R.id.image)
    private val itemNameTextView: TextView = itemView.findViewById(R.id.item_name_text)
    private val priceTextView: TextView = itemView.findViewById(R.id.item_price_text)
    private val shopNameTextView: TextView = itemView.findViewById(R.id.shop_name_text)
    private val favoriteButton: ImageButton = itemView.findViewById(R.id.favorite_button)

    init {
        favoriteButton.setOnClickListener { (item as? RankingItem)?.onFavoriteButtonClicked() }
    }

    fun bind(state: RankingItemState) {
        itemNameTextView.text = state.itemName
        priceTextView.text = "¥ ${String.format(Locale.US, "%,d", state.price)}"
        shopNameTextView.text = state.shopName
        favoriteButton.isSelected = state.isFavorite

        Glide.with(itemView)
            .load(state.imageUrl)
            .centerInside()
            .into(imageView)
    }
}