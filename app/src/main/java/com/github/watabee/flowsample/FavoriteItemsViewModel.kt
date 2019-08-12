package com.github.watabee.flowsample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.github.watabee.flowsample.db.FavoriteItem
import com.github.watabee.flowsample.db.FavoriteItemDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@UseExperimental(ExperimentalCoroutinesApi::class)
class FavoriteItemsViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteItemDao: FavoriteItemDao = (application as FlowSampleApp).favoriteItemDao

    val state: LiveData<FavoriteViewState>

    init {
        state = liveData {
            favoriteItemDao.findAllItems()
                .map { FavoriteViewState(it.toItemStates()) }
                .catch { FavoriteViewState(isError = true) }
                .flowOn(Dispatchers.IO)
                .collect { emit(it) }
        }
    }

    fun toggleFavorite(state: ItemState) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteItemDao.toggle(
                FavoriteItem(
                    state.itemCode,
                    state.imageUrl,
                    state.itemName,
                    state.shopName,
                    state.price
                )
            )
        }
    }

    private fun List<FavoriteItem>.toItemStates(): List<ItemState> =
        map {
            ItemState(
                itemCode = it.itemCode,
                imageUrl = it.imageUrl,
                itemName = it.itemName,
                price = it.price,
                shopName = it.shopName,
                isFavorite = true
            )
        }
}