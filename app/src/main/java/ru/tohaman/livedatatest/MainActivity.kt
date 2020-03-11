package ru.tohaman.livedatatest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.tohaman.livedatatest.data.TestDB
import ru.tohaman.livedatatest.data.testDatabase
import ru.tohaman.livedatatest.ui.main.MainFragment
import timber.log.Timber
import timber.log.Timber.DebugTree


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        TestDB.fillDb()

        if (savedInstanceState == null) {
            Timber.d("Создаем фрагмент")
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }

}
