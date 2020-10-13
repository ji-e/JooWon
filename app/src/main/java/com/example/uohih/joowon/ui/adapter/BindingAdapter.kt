package com.example.uohih.joowon.ui.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import org.apache.poi.ss.formula.functions.T

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("format", "argId")
    fun setFormattedText(textView: TextView, format: String, argId: Int?) {
        if (argId == null) {
            textView.text = String.format(format, "")
        } else {
            if (argId == 0) return

            textView.text = String.format(format, textView.resources.getString(argId))
        }
    }

    @JvmStatic
    @BindingAdapter("listData", "index")
    fun bindRecyclerView(recyclerView: RecyclerView, data: List<T>?, index: String) {
        if ("mainList" == index) {
            val adapter = recyclerView.adapter as MainListAdapter
            adapter.submitList(data)
        }
    }

}
