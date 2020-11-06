package com.example.uohih.joowon.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.uohih.joowon.util.LogUtil
import java.lang.Exception

class BaseRecyclerView {
    open class Adapter<ITEM : Any, B : ViewDataBinding>(
            @LayoutRes private val layoutResId: Int,
            private val bindingVariableId: Int? = null
    ) : RecyclerView.Adapter<ViewHolder<B>>() {
        private val items = mutableListOf<ITEM>()

        fun replaceAll(item: List<ITEM>?) {
            LogUtil.e(item.toString())
            item?.let {
                this.items.run {
                    clear()
                    addAll(it)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : ViewHolder<B>(
                layoutResId = layoutResId,
                parent = parent,
                bindingVariableId = bindingVariableId
        ) {}

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
            holder.onBindViewHolder(items[position])
        }


    }


    open class ViewHolder<B : ViewDataBinding>(
            @LayoutRes layoutResId: Int,
            parent: ViewGroup,
            private val bindingVariableId: Int?
    ) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
    ) {

        protected val binding: B = DataBindingUtil.bind(itemView)!!

        fun onBindViewHolder(item: Any?) {
            try {
                bindingVariableId?.let {
                    binding.setVariable(it, item)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}