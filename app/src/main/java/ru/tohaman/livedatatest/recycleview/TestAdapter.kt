package ru.tohaman.livedatatest.recycleview

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rcv_item.view.*
import ru.tohaman.livedatatest.R
import ru.tohaman.livedatatest.data.TestItem

class TestAdapter : RecyclerView.Adapter<TestAdapter.TestHolder>() {

    private var items: List<TestItem> = ArrayList()

    //создает RecyclerView.ViewHolder и инициализирует views для списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestHolder {
        return TestHolder (
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rcv_item, parent, false)
        )
    }

    //связывает views с содержимым
    override fun onBindViewHolder(viewHolder: TestHolder, position: Int) {
        viewHolder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    //передаем новые данные и оповещаем адаптер о необходимости обновления списка
    fun refreshItems(items: List<TestItem>) {
        this.items = items
        notifyDataSetChanged()
    }


    //внутренний класс ViewHolder описывает элементы представления списка и привязку их к RecyclerView
    class TestHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: TestItem) = with(itemView) {
            idText.text = item.num.toString()
            stText.text = item.st
        }
    }
}