package ru.tohaman.livedatatest.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.tohaman.livedatatest.TAG
import ru.tohaman.livedatatest.applicationLiveData
import ru.tohaman.livedatatest.getApplication
import ru.tohaman.livedatatest.ioThread
import timber.log.Timber

private const val DATABASE_NAME = "base.db"

val testDatabase : TestDB by lazy { buildDatabase(applicationLiveData.getApplication()) }

private fun buildDatabase(context: Context) : TestDB =
    Room.databaseBuilder(context, TestDB::class.java, DATABASE_NAME)
        .addCallback(object : RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                Timber.d("ReCreate database!!! Fill with new data ${applicationLiveData.getApplication()}")
                TestDB.fillDb()
            }
        })
        .build()



@Database (entities = [TestItem::class], version = 1)
abstract class TestDB : RoomDatabase() {
    abstract val testDao : TestItemDao

    companion object {
        private var instance : TestDB? = null
        @Synchronized
        fun get() : TestDB {
            if (instance == null) {
                instance = Room.databaseBuilder(applicationLiveData.getApplication(),
                    TestDB::class.java, DATABASE_NAME)
                    .addCallback(object : RoomDatabase.Callback(){
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            fillDb()
                        }
                    }).build()
            }
            return instance!!
        }

        fun fillDb() {
            ioThread {
                Timber.tag(TAG).d("insert data to DB")
                TestDB.get().testDao.insert(TestItem(0, 1,"First1"))
                testDatabase.testDao.insert(TestItem(0, 1,"First2"))
                testDatabase.testDao.insert(TestItem(0, 1,"First3"))
                testDatabase.testDao.insert(TestItem(0, 2,"Second1"))
                testDatabase.testDao.insert(TestItem(0, 2,"Second2"))
                testDatabase.testDao.insert(TestItem(0, 2,"Second3"))
                testDatabase.testDao.insert(TestItem(0, 3,"Third1"))
                testDatabase.testDao.insert(TestItem(0, 3,"Third2"))
                testDatabase.testDao.insert(TestItem(0, 3,"Third3"))
                testDatabase.testDao.insert(TestItem(0, 3,"Third4"))
            }
        }
    }
}