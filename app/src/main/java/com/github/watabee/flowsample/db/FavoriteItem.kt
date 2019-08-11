package com.github.watabee.flowsample.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_items"
)
data class FavoriteItem(
    @PrimaryKey val itemCode: String,
    val imageUrl: String?,
    val itemName: String,
    val shopName: String,
    val price: Int
)