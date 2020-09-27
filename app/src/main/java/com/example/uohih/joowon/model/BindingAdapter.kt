package com.example.uohih.joowon.model

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

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
}
