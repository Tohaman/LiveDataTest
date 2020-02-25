package ru.tohaman.livedatatest.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TestItem (
    @PrimaryKey (autoGenerate = true)
    var id : Int,   //Поле первичный ключ + автогенерируемое значение,
    var num: Int = 1,
    var st : String = "First"

)