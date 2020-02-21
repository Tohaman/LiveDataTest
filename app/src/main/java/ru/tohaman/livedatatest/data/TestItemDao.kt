package ru.tohaman.livedatatest.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TestItemDao {
    @Query("SELECT * FROM TestItem")
    fun getAllItems(): LiveData<List<TestItem>>

    @Query("SELECT * FROM TestItem WHERE num = :num")
    fun getByNum(num : Int) : LiveData<List<TestItem>>

    @Insert
    fun insert(testItem: TestItem?)
}