package ru.tohaman.livedatatest.data

import ru.tohaman.livedatatest.ioThread

class TestRepository {
    private val dao = testDatabase.testDao

    suspend fun getAllItems(): List<TestItem> {
        return dao.getAllItems()
    }

    suspend fun getByNum(num : Int) : List<TestItem> {
        return dao.getByNum(num)
    }

    suspend fun insert(testItem: TestItem?) {
        dao.insert(testItem)
    }
}