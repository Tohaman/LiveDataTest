package ru.tohaman.livedatatest

import android.app.Application
import timber.log.Timber

class TestApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        //Если приложение в дебаг версии, то выводим логи, если release, то не выводим
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        Domain.integrateWith(this)
    }
}