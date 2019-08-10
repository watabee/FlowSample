package com.github.watabee.flowsample

import com.github.watabee.flowsample.api.RankingResponse

data class MainViewState(
    val items: List<RankingResponse.Item>,
    val error: Boolean
)