package ru.tohaman.livedatatest.ui.main

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.tohaman.livedatatest.data.*
import ru.tohaman.livedatatest.ioThread
import ru.tohaman.livedatatest.utils.toMutableLiveData
import timber.log.Timber
import kotlin.random.Random

/**
 * Создаем viewModel - класс, который будет управлять нашим простым приложением, но при этом
 * он ничего не знает о том, как отображать данные, т.е. о View (UI), т.е. если мы что-то захотим
 * добавить во view или убрать, нам не надо переписывать управление (ViewModel).
 */


class MainViewModel : ViewModel() {

    private val dao = testDatabase.testDao
    private val repository = TestRepository()
    //Переменная, на которую можно подписаться из фрагмента/активити, но без автообновлений в биндинге
    var count = mCount.toMutableLiveData()

    //А такая будет автообноляться и в биндинге
    var observableCount = ObservableField<Int>(mCount)

    //Переменная с автообновлением в бинндинге собственного типа
    var obsTestItem = (TestItem(0, mCount, mCount.toString())).toMutableLiveData()

    //На такую переменную не подписаться, т.к. она private, но можно получить через public функцию (getHelloText)
    private val helloText = MutableLiveData<String>("Начальные значения - ${count.value} + ${obsTestItem.value?.st}")

    //Зададим mutableLiveData, чтобы на нее можно было подписаться и получать обновления
    var itemsList : MutableLiveData<List<TestItem>> = MutableLiveData()

    //Сделаем MutableLiveData<String> из count, преобразуем Int в String
    private val mutCount = MutableLiveData<String>("${count.value}")

    //Сделаем LiveData из MutableLiveData
    val liveCount: LiveData<Int>
        get() = count

    var check = MutableLiveData<Boolean>(true)

    var checked: Boolean
        get() {
            Timber.d("Get Checked")
          return true
        }
        set(value) {
            Timber.d("CorenerBuffer Changed - $value")
            //cornerBufferSet = value
        }

    init {
        Timber.d("MainViewModel - mCount=$mCount, but count.value=${count.value} and obsTestItem=${obsTestItem.value}")
        //но на данный момент переменные для биндинга = null (см.запись в логе), поэтому делаем так.
        count.value = mCount
        observableCount.set(count.value)
        obsTestItem.value = TestItem(0, mCount, mCount.toString())
        //Изначально возьмем значения не из базы room, а из простого списка
        itemsList.value = UserData.getList2()
    }

    //Можно получить значение через функцию, а можно и напрямую подисаться на переменную во фрагменте
    fun getListItems() = itemsList

    fun getHelloText() : LiveData<String> {
        return  helloText
    }

    private fun loadData(rnd : Int) {
        helloText.value = "New Int Value Generated - $rnd"
    }

    /** Метод не обязательно должен наследовать OnClickListener. Но должен быть public и иметь те же параметры,
    * что и метод OnClickListener.onClick(View view), т.е. должен быть один параметр типа View. Имя метода может быть любым.
    * вызов в лэйауте выглядит так: android:onClick="@{viewModel::onButtonClick}"
    * а если в layout использовать такой вызов android:onClick="@{(view) -> viewModel.onButtonClick(view)}"
    * то можно обойтись и без ненужного в данном случае параметра view, т.е. android:onClick="@{() -> viewModel.onButtonClick2()}"
    */

    fun onButtonClick(view : View) {
        Timber.d("onButtonClick")
        onButtonClick2()
    }

    fun onButtonClick2() {
        Timber.d("onButtonClick2 ${check.value}")
        val rnd = Random.nextInt(1,10) //Случайное число от 1 до 10
        val ost = (rnd - 1) % 4 + 1  //остаток от деления на 4, но поскольку у нас num в базе от 1 до 4, то делаем -1 и +1
        loadData(rnd)
        count.value = rnd
        observableCount.set(rnd)
        obsTestItem.value = TestItem(0, rnd, rnd.toString())
        //Поскольку простой List из базы room нельзя получить в основном потоке (LiveData можно), то используем такое расширение функций котлина
        //В реальных условиях, лучше использовать отдельный класс "репозиторий", в котором использовать suspend функции для
        //получения таких данных, например как тут https://github.com/OmneyaOsman/PetsShelter
        viewModelScope.launch {
            //получаем список по остатку от деления нашего случаайного числа
            val lst = repository.getByNum(ost)
            Timber.d("lst = $lst")
            //И опять же, поскольку поток не основной, то не можем использовать itemList.value = ...  ,
            // т.е. itemList.setValue(...), соотвественно используем postValue
            itemsList.postValue(lst)
        }
    }


    companion object {
        /**
         * Создаем фабрику, чтобы можно было создать viewModel и сразу передать каие-то переменные при создании объекта
         * или задать какие-то по-умолчанию. Т.е. в данном случае при создании объекта не через фабрику
         * [count] будет = 0, а при создании через фабрику параметр [count] обязательный и не задать его нельзя.
         * Можем сделать конструктор приватным, чтобы нельзя было создать объект без фабрики
         */
        var mCount : Int = 0

        class MainViewModelFactory (private val count: Int) : NewInstanceFactory() {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                Timber.d("MainViewModelFactory - Create $count")
                mCount = count
                //Преобразуем в MutableLiveData через KotlinExtensions, которые заданы в LiveDataExtensions.kt
                return MainViewModel() as T
            }
        }
    }
}
