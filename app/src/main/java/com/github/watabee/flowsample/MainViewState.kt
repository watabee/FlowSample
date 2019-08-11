package com.github.watabee.flowsample

data class MainViewState(
    val rankingItemStates: List<RankingItemState> = emptyList(),
    val isError: Boolean = false,
    val isLoading: Boolean = false
)