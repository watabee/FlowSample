package com.github.watabee.flowsample.api

import retrofit2.http.GET
import retrofit2.http.Query

interface RakutenApi {

    @GET("/services/api/IchibaItem/Ranking/20170628")
    suspend fun findRankingItems(
        @Query("format") format: String = "json",
        @Query("formatVersion") formatVersion: String = "2",
        @Query("applicationId") applicationId: String
    ): RankingResponse
}