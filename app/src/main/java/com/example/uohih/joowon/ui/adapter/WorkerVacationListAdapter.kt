package com.example.uohih.joowon.ui.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.database.VacationData
import kotlinx.android.synthetic.main.list_item_main_list.view.mainList_item
import kotlinx.android.synthetic.main.list_item_main_list.view.mainList_leftView
import kotlinx.android.synthetic.main.list_item_main_list.view.mainList_rightView
import kotlinx.android.synthetic.main.list_item_main_list.view.mainList_swipeLayout
import kotlinx.android.synthetic.main.list_item_worker_main.view.*
import java.util.*

/**
 * WorkerVacationListAdapter 아답터(직원 리스트)
 */
class WorkerVacationListAdapter(private val vacationDataList: ArrayList<VacationData>)
    : RecyclerView.Adapter<WorkerVacationListAdapter.ViewHolder>() {

    private var mListener: ClickListener? = null

    interface ClickListener {
        fun onmClickEvent(bundle: Bundle)
    }

    fun setClickListener(listener: ClickListener) {
        this.mListener = listener
    }

    /**
     * 데이터 삭제
     * position: Int: 삭제할 index
     */
    fun removeAt(position: Int) {
        vacationDataList.removeAt(position)
        notifyDataSetChanged()
    }

    private val itemsOffset = IntArray(itemCount)
    private lateinit var mContext: Context

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        val mSwipeLayout = view.mainList_swipeLayout
        val mLayoutLeft = itemView.mainList_leftView      //왼쪽 스와이프
        val mLayoutRight = itemView.mainList_rightView    //오른쪽 스와이프

        val mTvNo = view.worker_vacation_list_no               //
        val mTvDate = view.worker_vacation_list_date           //휴가사용날
        val mTvContent = view.worker_vacation_list_content     //휴가내용
        val mTvUse = view.worker_vacation_list_use             //휴가사용개


        fun bind(listener: View.OnClickListener, item: VacationData, mContext: Context) {
            mTvNo.text = String.format("%02d", (position + 1))
            mTvDate.text = (Constants.YYYYMMDD_PATTERN).toRegex().replace((item.date).toString(), "$1-$2-$3")
            mTvContent.text = (item.content)
            mTvUse.text = (item.use).toString()

            view.mainList_item.setOnClickListener(listener)

            mLayoutLeft.isClickable = true
            mLayoutLeft.setOnClickListener(listener)

            mLayoutRight.isClickable = true
            mLayoutRight.setOnClickListener(listener)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_worker_main, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = vacationDataList[position]
        val listener = View.OnClickListener {
            when (it.id) {
            }
        }
        holder.mSwipeLayout.animateReset()


        holder.apply {
            bind(listener, item, mContext)
            itemView.tag = item
        }

    }

    override fun getItemCount(): Int {
        return vacationDataList.size
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
//        if (holder.adapterPosition != RecyclerView.NO_POSITION) {
//            itemsOffset[holder.adapterPosition] = holder.mSwipeLayout.offset
//        }
    }
}

