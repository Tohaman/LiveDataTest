package ru.tohaman.livedatatest.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("showInt")
fun showInt(textView: TextView, count: Int?) {
    count?.let {
        textView.text = "$it"
    }
}

@BindingAdapter("dateToString")
fun dateToString(textView: TextView, date: Date?) {
    date?.let {
        textView.text = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(it)
    }
}

@BindingAdapter("showTime")
fun showTime(textView: TextView, time: Long?) {
    time?.let {
        textView.text = "$it сек"
    }
}