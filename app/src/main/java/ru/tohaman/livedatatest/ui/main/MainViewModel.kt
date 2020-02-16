package ru.tohaman.livedatatest.ui.main

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import ru.tohaman.livedatatest.utils.toMutableLiveData
import timber.log.Timber
import kotlin.random.Random

/**
 * Создаем viewModel - класс, который будет управлять нашим простым приложением, но при этом
 * он ничего не знает о том, как отображать данные, т.е. о View (UI), т.е. если мы что-то захотим
 * добавить во view или убрать, нам не надо переписывать управление
 */


class MainViewModel(private val _count: MutableLiveData<Int> = MutableLiveData()) : ViewModel() {

    //Переменная, на которую можно подписаться из фрагмента/активити, но без автообновлений в биндинге
    var count = _count

    //А такая будет автообноляться и в биндинге
    var observableCount = ObservableField<Int>()

    init {
        count = _count
        Timber.d("MainViewModel - init ${count.value}")
        observableCount.set(count.value)
    }

    //На такую переменную не подписаться, т.к. она private, но можно получить через public функцию (getHelloText)
    private val helloText = MutableLiveData<String>("Hello World - ${count.value}")

    fun getHelloText() : LiveData<String> {
        return  helloText
    }

    private fun loadData(rnd : Int) {
        helloText.value = "New Int Value Generated - $rnd"
    }

    /** Метод не обязательно должен наследовать OnClickListener. Но должен быть public и иметь те же параметры,
    * что и метод OnClickListener.onClick(View view), т.е. должен быть один параметр типа View. Имя метода может быть любым.
    * вызов в лэйауте выглядит так: android:onClick="@{viewModel::onButtonClick}"
    * а если использовать такой вызов android:onClick="@{(view) -> viewModel.onButtonClick(view)}"
    * то можно обойтись и без ненужного в данном случае параметра view, т.е. android:onClick="@{() -> viewModel.onButtonClick2()}"
    */

    fun onButtonClick(view : View) {
        Timber.d("onButtonClick")
        val rnd = Random.nextInt(1,10)
        loadData(rnd)
        count.value = rnd
        observableCount.set(rnd)
    }

    fun onButtonClick2() {
        Timber.d("onButtonClick2")
        val rnd = Random.nextInt(1,10)
        loadData(rnd)
        count.value = rnd
        observableCount.set(rnd)
    }


    companion object {
        /**
         * Создаем фабрику, чтобы можно было создать viewModel и сразу передать каие-то переменные при создании объекта
         * или задать какие-то по-умолчанию. Т.е. в данном случае при создании объекта не через фабрику
         * [count] будет пустым, а при создании через фабрику параметр [count] обязательный
         */
        class MainViewModelFactory (private val count: Int) : NewInstanceFactory() {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                Timber.d("MainViewModelFactory - Create $count")
                //Преобразуем в MutableLiveData через KotlinExtensions, которые заданы в LiveDataExtensions.kt
                return MainViewModel(count.toMutableLiveData()) as T
            }
        }
    }
}