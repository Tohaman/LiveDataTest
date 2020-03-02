package ru.tohaman.livedatatest.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Интерфейс для работы с базой Room, если запрос возвращает не LiveData<>, то он должен выполняться
 * не в основном потоке.
 * Для вставки/обновления/удаления используются методы insert/update/delete с соответствующими
 * аннотациями. Тут никакие запросы указывать не нужно. Названия методов могут быть любыми.
 * Главное - аннотации.
 * Имя таблицы = имя класса помеченного как @Entity или заданное в нем через tableName = "mainTable"
 */

@Dao
interface TestItemDao {
    //Возвращаем список всех записей из базы
    @Query("SELECT * FROM TestItem")
    suspend fun getAllItems(): List<TestItem>

    //Только записи с опредленным значением num
    @Query("SELECT * FROM TestItem WHERE num = :num")
    suspend fun getByNum(num : Int) : List<TestItem>

    //Добавляем запись в базу
    @Insert
    suspend fun insert(testItem: TestItem?)
}