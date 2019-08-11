package com.github.watabee.flowsample

import android.view.View
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
    val shopName: String
)

class RankingItem(
    private val state: RankingItemState
) : Item<RankingViewHolder>(state.itemCode.hashCode().toLong()) {
    override fun getLayout(): Int = R.layout.list_item_ranking

    override fun createViewHolder(itemView: View): RankingViewHolder = RankingViewHolder(itemView)

    override fun bind(viewHolder: RankingViewHolder, position: Int) {
        viewHolder.bind(state)
    }
}

class RankingViewHolder(itemView: View) : ViewHolder(itemView) {

    private val imageView: ImageView = itemView.findViewById(R.id.image)
    private val itemNameTextView: TextView = itemView.findViewById(R.id.item_name_text)
    private val priceTextView: TextView = itemView.findViewById(R.id.item_price_text)
    private val shopNameTextView: TextView = itemView.findViewById(R.id.shop_name_text)

    fun bind(state: RankingItemState) {
        itemNameTextView.text = state.itemName
        priceTextView.text = "¥ ${String.format(Locale.US, "%,d", state.price)}"
        shopNameTextView.text = state.shopName

        Glide.with(itemView)
            .load(state.imageUrl)
            .centerInside()
            .into(imageView)
    }
}