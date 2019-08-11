package com.github.watabee.flowsample.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteItemDao {

    @Query("SELECT * FROM favorite_items")
    fun findAllItems(): Flow<List<FavoriteItem>>

    @Query("SELECT itemCode FROM favorite_items")
    fun findAllItemCodes(): Flow<List<String>>

    @Query(
        """
        SELECT CASE WHEN COUNT(*) = 0 THEN 0 ELSE 1 END
        FROM favorite_items
        WHERE itemCode = :itemCode
    """
    )
    fun existsItem(itemCode: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: FavoriteItem)

    @Delete
    fun deleteItem(item: FavoriteItem)

    @Transaction
    fun toggle(item: FavoriteItem) {
        if (existsItem(item.itemCode)) {
            deleteItem(item)
        } else {
            insertItem(item)
        }
    }
}