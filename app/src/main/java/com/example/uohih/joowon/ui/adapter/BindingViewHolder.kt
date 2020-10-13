package com.example.uohih.joowon.ui.adapter

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


class BindingViewHolder<T : ViewDataBinding?>(view: View) : RecyclerView.ViewHolder(view) {
    private val binding: T
    fun binding(): T {
        return binding
    }

    init {
        binding = DataBindingUtil.bind<ViewDataBinding>(view) as T
    }
}