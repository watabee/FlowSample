package com.github.watabee.flowsample.api

import android.content.Context
import com.github.watabee.flowsample.BuildConfig
import com.squareup.moshi.Moshi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

interface RankingRepository {

    fun findRankingItems(): Flow<RankingResponse>
}

class RankingDataSource(context: Context) : RankingRepository {

    private val api: RakutenApi

    init {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .cache(Cache(context.cacheDir, 10 * 1024 * 1024))
            .addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) = Timber.tag("OkHttp").w(message)
            }).apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val moshi: Moshi = Moshi.Builder().build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://app.rakuten.co.jp")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(RakutenApi::class.java)
    }

    @UseExperimental(FlowPreview::class)
    override fun findRankingItems(): Flow<RankingResponse> =
        this::findRankingItemsInternal.asFlow()

    private suspend fun findRankingItemsInternal(): RankingResponse =
        api.findRankingItems(applicationId = BuildConfig.RAKUTEN_APP_ID)
}