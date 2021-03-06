package ru.tohaman.livedatatest.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.main_fragment.*
import ru.tohaman.livedatatest.data.TestItem
import ru.tohaman.livedatatest.databinding.MainFragmentBinding
import ru.tohaman.livedatatest.recycleview.TestAdapter
import ru.tohaman.livedatatest.utils.toMutableLiveData
import timber.log.Timber
import kotlin.random.Random

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    //private lateinit var viewModel: MainViewModel
    //или инициализируем через by viewModels, но для этого надо добавить implementation "androidx.navigation:navigation-fragment-ktx:2.2.1" в build.gradle
    private val viewModel : MainViewModel by viewModels{
        val rnd = Random.nextInt(30,40)
        Timber.d("by viewModels with $rnd")
        MainViewModel.Companion.MainViewModelFactory(rnd)
    }
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        Timber.d("onCreateView")
        viewModel.count.value = 1
        //создаем объект биндинга, имя генерируемый класс = имя соответствующего лэйаута (с большими буквами) + Binding
        binding = MainFragmentBinding.inflate(inflater, container, false)
            //.apply { lifecycleOwner = this@MainFragment }
        //возвращаем view, т.е. root элемент нашего биндинга

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        //можно вызвать viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        //но в этом случае viewModel.count не будет сразу инициализирован

        //val rnd = Random.nextInt(1,10)
        //viewModel = ViewModelProvider(this, MainViewModel.Companion.MainViewModelFactory(rnd)).get(MainViewModel::class.java)
        //передаем viewModel в биндинг (один раз и все, больше его не обновляем)
        Timber.d("binding.viewModel")
        binding.viewModel = viewModel
        //Если мы зададим viewModel.observableCount.set(56), то оно сразу отобразится, иначе оно будет null
        //viewModel.observableCount.set(56)

        //Казалось бы можно сделать так
        //binding.testItem = viewModel.obsTestItem().value
        //но в этом случае, не будет автообновления переменной, поэтому подписываемся на obsTestItem
        val dataTestItem : LiveData<TestItem> = viewModel.obsTestItem
        dataTestItem.observe(viewLifecycleOwner, Observer { binding.testItem = it })

        //инициализируем адаптер и присваиваем его списку
        val adapter = TestAdapter()
        rcv.layoutManager = LinearLayoutManager (context)
        rcv.adapter = adapter
        initSwipe(rcv)

        //подписываем адаптер на изменения списка
        viewModel.itemsList.observe(viewLifecycleOwner, Observer {
            it?.let {
                Timber.d ("Сработало обновление списка для адаптера - $it")
                adapter.refreshItems(it)
            }
        })

        //но можем связать какое-то свойство визуального объекта и не через биндинг, а по-старинке
        //вместо findViewById просто используем id элемента, например, message.text = ....
        //Поскольку связываем через data.observe, то при изменении viewModel.helloText текст во View тоже будет меняться
        Timber.d("observe data<String>")
        val data : LiveData<String> = viewModel.getHelloText()
        data.observe(viewLifecycleOwner, Observer {message.text = it})

        //count.text зададим напрямую в лэйауте android:text="@{viewModel.count.toString()}"
        //т.к. там мы связали (bind) с полем типа MutableLiveData, то автоматом это поле обновляться при изменении значения не будет

        //count2.text свяжем с полем типа ObservableField<Int>(), и оно уже будет автообновляться

        /**текст для [count4] зададим не через биндинг, а привяжем LiveData<Int>, который будет изменяться автоматически*/
        viewModel.liveCount.observe(viewLifecycleOwner) { value ->
            count4.text = value.toString()
        }

        /** Если нам необходимо использовать список, как отдельную переменную в layout,
         * например, аналог listOfSomeString : List<String>
         * в variable лэйаута будет выглядеть так:
        <variable
            name="listOfSomeString"
            type="java.util.List&lt;String&gt;" />
        Ограничения XML не позволяют просто так использовать символы < и >. Поэтому их приходится заменять спецсимволами &lt; и &gt;.
        подробнее можно посмотреть на https://startandroid.ru/ru/courses/architecture-components/27-course/architecture-components/552-urok-19-android-data-binding-vozmozhnosti.html
        */


    }

    private fun initSwipe(rcView: RecyclerView) {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            // Определяем в каких направлениях будет разрешено перетаскивать элементы https://habr.com/ru/post/427681/
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, 0)
            }


            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition

                //TODO Поменять местами элементы в List и базе
                //Collections.swap(mutableList, fromPosition, toPosition)
                //recyclerView.adapter.mo
                recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        }).attachToRecyclerView(rcView)
    }

}
