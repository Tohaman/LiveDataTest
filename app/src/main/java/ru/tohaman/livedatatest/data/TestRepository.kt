package ru.tohaman.livedatatest.data

import kotlinx.coroutines.delay

/**
 * Для этого примера казалось бы лишний класс, который в реальных условиях позволяет
 * реализовать, например кэширование данных. Т.е. программа обращается за данными в
 * репозиторий, а уже репозиторий решает откуда их брать (из локальной базы, интернета
 * или откуда-то еще). Подробнее тут (англ). https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7
 *
 * Сразу пропишем функции репозитория как suspend, чтобы было понятно, что их нельзя
 * вызывать из основного потока с его блокировкой. Например, для вызова из viewModel, лучше использовать что-то типа
 *  viewModelScope.launch {
 *      ...
 *      val someList = repository.getAllItems()
 *      ...
 *  }
 *  в этом случае, при уничтожении viewModel, например, при закритии приложения во время выполнения
 *  запроса, запрос автомтически будет прерван ("чудеса корутин").
 */


class TestRepository {
    private val dao = testDatabase.testDao

    suspend fun getAllItems(): List<TestItem> {
//        delay(5000)
        return dao.getAllItems()
    }

    suspend fun getByNum(num : Int) : List<TestItem> {
        //Имитирунм задержку получения данных
        //delay(2000) //2 сек
        return dao.getByNum(num)
    }

    suspend fun insert(testItem: TestItem?) {
        dao.insert(testItem)
    }
}