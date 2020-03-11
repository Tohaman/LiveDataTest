package ru.tohaman.livedatatest.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ДатаКласс используемый для создания записей в таблице.
 * В качестве имени таблицы будет использовано имя класса. А поля таблицы будут созданы в соответствии с полями класса.
 * Аннотацией PrimaryKey мы помечаем поле, которое будет ключом в таблице.
 * Можно поменять имя и задать комбинированный ключ, например
 * @Entity (tableName = "mainTable", primaryKeys = ["num", "st"])
 */

@Entity
data class TestItem (
    @PrimaryKey (autoGenerate = true)
    var id : Int,   //Поле первичный ключ + автогенерируемое значение,
    var num: Int = 1,
    var st : String = "First"
)