package com.github.watabee.flowsample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.github.watabee.flowsample.api.RankingDataSource
import com.github.watabee.flowsample.api.RankingResponse
import com.github.watabee.flowsample.db.AppDatabase
import com.github.watabee.flowsample.db.FavoriteItem
import com.github.watabee.flowsample.db.FavoriteItemDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@UseExperimental(ExperimentalCoroutinesApi::class, FlowPreview::class)
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteItemDao: FavoriteItemDao =
        AppDatabase.getInstance(application).favoriteItemDao()
    private val rankingRepository = RankingDataSource(application)
    private val requestEvent = ConflatedBroadcastChannel<Unit>()

    val state: LiveData<MainViewState>

    init {
        state = liveData {
            requestEvent.asFlow()
                .onEach { emit(MainViewState(isLoading = true)) }
                .flatMapLatest {
                    rankingRepository.findRankingItems()
                        .combine(favoriteItemDao.findAllItemCodes()) { rankingResponse, favoriteItemCodes ->
                            MainViewState(
                                rankingResponse.items.toRankingItemStates(favoriteItemCodes)
                            )
                        }
                        .catch { emit(MainViewState(isError = true)) }
                        .flowOn(Dispatchers.IO)
                }
                .collect { emit(it) }
        }

        findRankingItems()
    }

    fun findRankingItems() = requestEvent.offer(Unit)

    fun toggleFavorite(state: RankingItemState) {
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

    private fun List<RankingResponse.Item>.toRankingItemStates(
        favoriteItemCodes: List<String>
    ): List<RankingItemState> =
        map {
            RankingItemState(
                itemCode = it.itemCode,
                imageUrl = it.mediumImageUrls.firstOrNull() ?: it.smallImageUrls.firstOrNull(),
                itemName = it.itemName,
                price = it.itemPrice.toInt(),
                shopName = it.shopName,
                isFavorite = favoriteItemCodes.contains(it.itemCode)
            )
        }
}

