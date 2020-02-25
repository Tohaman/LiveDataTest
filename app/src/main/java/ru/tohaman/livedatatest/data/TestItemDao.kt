package ru.tohaman.livedatatest.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TestItemDao {
    //Возвращаем список всех записей из базы
    @Query("SELECT * FROM TestItem")
    fun getAllItems(): List<TestItem>

    //Только записи с опредленным значением num
    @Query("SELECT * FROM TestItem WHERE num = :num")
    fun getByNum(num : Int) : List<TestItem>

    //Добавляем запись в базу
    @Insert
    fun insert(testItem: TestItem?)
}