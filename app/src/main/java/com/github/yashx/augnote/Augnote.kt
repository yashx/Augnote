package com.github.yashx.augnote

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.github.yashx.augnote.utils.PrefHelper
import org.koin.android.ext.android.get
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

        AppCompatDelegate.setDefaultNightMode(
            if (get<PrefHelper>().isInDarkMode)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}