package com.example.uohih.joowon.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.uohih.joowon.R
import com.example.uohih.joowon.databinding.ViewpagerWorkerMainListItemBinding
import com.example.uohih.joowon.model.VacationList
import com.example.uohih.joowon.util.LogUtil
import java.util.*

/**
 * WorkerVacationListAdapter 아답터(직원휴가리스트)
 */
class WorkerVacationListAdapter(vacationList: ArrayList<VacationList>?, private val mContext: Context)
    : RecyclerView.Adapter<WorkerVacationListAdapter.ViewHolder>() {

    private var vacationList = arrayListOf<VacationList>()

    init {
        if (vacationList != null) {
            this.vacationList = vacationList
        }
    }

    fun setVacationList(vacationList: ArrayList<VacationList>?) {
        this.vacationList = vacationList ?: arrayListOf()
        notifyDataSetChanged()
    }

    override fun getItemCount() = vacationList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = vacationList[position]
        holder.tvNo.text = String.format("%2d", position + 1)

        holder.itemView.setOnClickListener {


        }

        holder.apply {
            bind(item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        return ViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.viewpager_worker_main_list_item, parent, false
                )
        )
    }

    class ViewHolder(
            private val binding: ViewpagerWorkerMainListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val tvNo = binding.viewpagerWorkerMainNo
        fun bind(item: VacationList) {
            binding.apply {
                workerMainViewpagerListVal = item
            }
        }
    }

}

