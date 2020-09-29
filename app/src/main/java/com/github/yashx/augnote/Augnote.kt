package com.github.yashx.augnote

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class Augnote : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@Augnote)
            koin.loadModules(listOf(appModule))
            koin.createRootScope()
        }
    }
}