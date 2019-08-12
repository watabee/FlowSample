package com.github.watabee.flowsample

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.xwray.groupie.ViewHolder
import java.util.Locale

data class ItemState(
    val itemCode: String,
    val imageUrl: String?,
    val itemName: String,
    val price: Int,
    val shopName: String,
    val isFavorite: Boolean
)

class Item(
    private val state: ItemState,
    private val onFavoriteButtonClicked: (state: ItemState) -> Unit
) : com.xwray.groupie.Item<ItemViewHolder>(state.itemCode.hashCode().toLong()) {
    override fun getLayout(): Int = R.layout.list_item

    override fun createViewHolder(itemView: View): ItemViewHolder = ItemViewHolder(itemView)

    override fun bind(viewHolder: ItemViewHolder, position: Int) {
        viewHolder.bind(state)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Item) {
            return false
        }
        return state == other.state
    }

    override fun hashCode(): Int = state.hashCode()

    fun onFavoriteButtonClicked() = onFavoriteButtonClicked(state)
}

class ItemViewHolder(itemView: View) : ViewHolder(itemView) {

    private val imageView: ImageView = itemView.findViewById(R.id.image)
    private val itemNameTextView: TextView = itemView.findViewById(R.id.item_name_text)
    private val priceTextView: TextView = itemView.findViewById(R.id.item_price_text)
    private val shopNameTextView: TextView = itemView.findViewById(R.id.shop_name_text)
    private val favoriteButton: ImageButton = itemView.findViewById(R.id.favorite_button)

    init {
        favoriteButton.setOnClickListener { (item as? Item)?.onFavoriteButtonClicked() }
    }

    fun bind(state: ItemState) {
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