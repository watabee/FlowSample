package com.github.watabee.flowsample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.github.watabee.flowsample.api.RankingDataSource
import com.github.watabee.flowsample.api.RankingResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.util.Locale

@UseExperimental(ExperimentalCoroutinesApi::class, FlowPreview::class)
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val rankingRepository = RankingDataSource(application)
    private val requestEvent = ConflatedBroadcastChannel<Unit>()

    val state: LiveData<MainViewState>

    init {
        state = liveData {
            requestEvent.asFlow()
                .onEach { emit(MainViewState(isLoading = true)) }
                .flatMapLatest {
                    rankingRepository.findRankingItems()
                        .map { MainViewState(it.items.toRankingItemStates()) }
                        .catch { emit(MainViewState(isError = true)) }
                        .flowOn(Dispatchers.IO)
                }
                .collect { emit(it) }
        }

        findRankingItems()
    }

    fun findRankingItems() = requestEvent.offer(Unit)

    private fun List<RankingResponse.Item>.toRankingItemStates(): List<RankingItemState> =
        map {
            RankingItemState(
                itemCode = it.itemCode,
                imageUrl = it.mediumImageUrls.firstOrNull() ?: it.smallImageUrls.firstOrNull(),
                itemName = it.itemName,
                price = it.itemPrice.toInt(),
                shopName = it.shopName
            )
        }
}

