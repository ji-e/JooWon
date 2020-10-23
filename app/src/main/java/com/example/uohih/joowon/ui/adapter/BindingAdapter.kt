package com.example.uohih.joowon.ui.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
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
    @BindingAdapter("replaceAll")
    fun RecyclerView.replaceAll(list: List<Nothing>?) {
        (this.adapter as? BaseRecyclerView.Adapter<*, *>)?.run {
            this.replaceAll(list)
            notifyDataSetChanged()
        }
    }

    @JvmStatic
    @BindingAdapter("replaceAll")
    fun ViewPager2.replaceAll(list: List<Nothing>?) {
        (this.adapter as? BaseRecyclerView.Adapter<*, *>)?.run {
            this.replaceAll(list)
            notifyDataSetChanged()
        }
    }


}
