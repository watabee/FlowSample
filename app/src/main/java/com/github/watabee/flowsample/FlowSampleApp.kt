package com.github.watabee.flowsample

import android.app.Application
import com.github.watabee.flowsample.db.AppDatabase
import com.github.watabee.flowsample.db.FavoriteItemDao
import timber.log.Timber

class FlowSampleApp : Application() {

    val favoriteItemDao: FavoriteItemDao by lazy(LazyThreadSafetyMode.NONE) {
        AppDatabase.getInstance(this).favoriteItemDao()
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}