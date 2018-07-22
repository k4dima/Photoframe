package com.k4dima.photoframe.features.app

import android.os.StrictMode
import com.k4dima.photoframe.BuildConfig
import com.k4dima.photoframe.features.app.di.DaggerAppComponent
import dagger.android.support.DaggerApplication

class App : DaggerApplication() {
    override fun applicationInjector() = DaggerAppComponent.create()!!
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG && false) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
        }
    }
}