package com.github.watabee.flowsample.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RankingResponse(
    @Json(name = "Items") val items: List<Item>,
    val lastBuildDate: String,
    val title: String
) {

    @JsonClass(generateAdapter = true)
    data class Item(
        val affiliateRate: String,
        val affiliateUrl: String,
        val asurakuArea: String,
        val asurakuClosingTime: String,
        val asurakuFlag: Int,
        val availability: Int,
        val carrier: Int,
        val catchcopy: String,
        val creditCardFlag: Int,
        val endTime: String,
        val genreId: String,
        val imageFlag: Int,
        val itemCaption: String,
        val itemCode: String,
        val itemName: String,
        val itemPrice: String,
        val itemUrl: String,
        val mediumImageUrls: List<String>,
        val pointRate: Int,
        val pointRateEndTime: String,
        val pointRateStartTime: String,
        val postageFlag: Int,
        val rank: Int,
        val reviewAverage: String,
        val reviewCount: Int,
        val shipOverseasArea: String,
        val shipOverseasFlag: Int,
        val shopCode: String,
        val shopName: String,
        val shopOfTheYearFlag: Int,
        val shopUrl: String,
        val smallImageUrls: List<String>,
        val startTime: String,
        val taxFlag: Int
    )
}