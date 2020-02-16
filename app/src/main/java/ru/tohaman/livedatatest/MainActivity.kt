package ru.tohaman.livedatatest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.tohaman.livedatatest.ui.main.MainFragment
import timber.log.Timber
import timber.log.Timber.DebugTree


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        //Если приложение в дебаг версии, то выводим логи, если release, то используем FakeCrashLibrary
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        if (savedInstanceState == null) {
            Timber.d("Создаем фрагмент")
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }

    /** A tree which logs important information for crash reporting.  */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(
            priority: Int,
            tag: String?,
            message: String,
            t: Throwable?
        ) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
            FakeCrashLibrary.log(priority, tag, message)
            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t)
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t)
                }
            }
        }
    }

}

class FakeCrashLibrary private constructor() {
    companion object {
        fun log(
            priority: Int,
            tag: String?,
            message: String?
        ) { // TODO add log entry to circular buffer.
        }

        fun logWarning(t: Throwable?) { // TODO report non-fatal warning.
        }

        fun logError(t: Throwable?) { // TODO report non-fatal error.
        }
    }

    init {
        throw AssertionError("No instances.")
    }
}