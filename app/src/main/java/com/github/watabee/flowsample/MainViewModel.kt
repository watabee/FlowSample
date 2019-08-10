package com.github.watabee.flowsample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.github.watabee.flowsample.api.RankingDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@UseExperimental(ExperimentalCoroutinesApi::class)
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val rankingRepository = RankingDataSource(application)

    val state: LiveData<MainViewState>

    init {
        state = liveData {
            rankingRepository.findRankingItems()
                .map { MainViewState(it.items, error = false) }
                .catch { emit(MainViewState(emptyList(), error = true)) }
                .flowOn(Dispatchers.IO)
                .collect { emit(it) }
        }
    }
}

