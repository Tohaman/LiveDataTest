package ru.tohaman.livedatatest.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.runBlocking
import ru.tohaman.livedatatest.TAG
import ru.tohaman.livedatatest.applicationLiveData
import ru.tohaman.livedatatest.getApplication
import ru.tohaman.livedatatest.ioThread
import timber.log.Timber

/**
 * Абстрактный класс базы, поскольку создание такого класса "дорогое удовольствие", то
 * используем или синглтон (синглет) через создание в companion object instance
 * или глабоальную переменную testDataBase
 */


private const val DATABASE_NAME = "base.db"

val testDatabase : TestDB by lazy { buildDatabase(applicationLiveData.getApplication()) }

private fun buildDatabase(context: Context) : TestDB =
    Room.databaseBuilder(context, TestDB::class.java, DATABASE_NAME)
        .addCallback(object : RoomDatabase.Callback(){
            //onOpen - срабатывает при каждом открытии программы, можно
            //поменять на onCreate, тогда будет срабатывать только при первом создании базы
            override fun onCreate(db: SupportSQLiteDatabase) {
                Timber.d("ReCreate database!!! Fill with new data ${applicationLiveData.getApplication()}")
                //заполняем базу какими-то записями
                TestDB.fillDb()
            }
        })
        .build()



@Database (entities = [TestItem::class], version = 1, exportSchema = false)
abstract class TestDB : RoomDatabase() {
    abstract val testDao : TestItemDao

    companion object {
        //Можно инициализировать так, а можно  через переменную testDatabase и fun buildDatabase

//        private var instance : TestDB? = null
//        @Synchronized
//        fun get() : TestDB {
//            if (instance == null) {
//                instance = Room.databaseBuilder(applicationLiveData.getApplication(),
//                    TestDB::class.java, DATABASE_NAME)
//                    .addCallback(object : RoomDatabase.Callback(){
//                        override fun onOpen(db: SupportSQLiteDatabase) {
//                            fillDb()
//                        }
//                    }).build()
//            }
//            return instance!!
//        }

        fun fillDb() {
            runBlocking {
                Timber.tag(TAG).d("insert data to DB")
                testDatabase.testDao.insert(TestItem(0, 1,"First 1"))
                testDatabase.testDao.insert(TestItem(0, 2,"Second 1"))
                testDatabase.testDao.insert(TestItem(0, 2,"Second 2"))
                testDatabase.testDao.insert(TestItem(0, 3,"Third 1"))
                testDatabase.testDao.insert(TestItem(0, 3,"Third 2"))
                testDatabase.testDao.insert(TestItem(0, 3,"Third 3"))
                testDatabase.testDao.insert(TestItem(0, 4,"Четыре 1"))
                testDatabase.testDao.insert(TestItem(0, 4,"Четыре 2"))
                testDatabase.testDao.insert(TestItem(0, 4,"Четыре 3"))
                testDatabase.testDao.insert(TestItem(0, 4,"Четыре 4"))
            }
        }
    }
}