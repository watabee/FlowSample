package com.github.watabee.flowsample

import android.app.Application
import timber.log.Timber

class FlowSampleApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}