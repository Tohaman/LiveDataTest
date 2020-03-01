package ru.tohaman.livedatatest.data

import kotlinx.coroutines.delay

/**
 * Для этого примера казалось бы лишний класс, который в реальных условиях позволяет
 * реализовать, например кэширование данных. Т.е. программа обращается за данными в
 * репозиторий, а уже репозиторий решает откуда их брать (из локальной базы, интернета
 * или откуда-то еще)
 */


class TestRepository {
    private val dao = testDatabase.testDao

    suspend fun getAllItems(): List<TestItem> {
//        delay(5000)
        return dao.getAllItems()
    }

    suspend fun getByNum(num : Int) : List<TestItem> {
        //Имитирунм задержку получения данных
        delay(2000) //2 сек
        return dao.getByNum(num)
    }

    suspend fun insert(testItem: TestItem?) {
        dao.insert(testItem)
    }
}