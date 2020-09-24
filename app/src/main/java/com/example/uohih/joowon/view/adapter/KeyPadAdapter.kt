package com.example.uohih.joowon.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity

/**
 * 숫자 키패드 아답터
 * mContext: Context
 * keyPadData: ArrayList<String>: 숫자 정보 리스트
 */
class KeyPadAdapter(private val mContext: Context, private var keyPadData: ArrayList<String>) : BaseAdapter() {

    val BaseActivity = JWBaseActivity()

    /**
     * ----------- 버튼클릭 리스너 start -----------
     */
    private var mListener: KeyPadListener? = null

    interface KeyPadListener {
        fun onNumClickEvent(index: String)
        fun onEraserClickEvent()
        fun onRefreshClickEvent()
    }

    fun setKeyPadListener(listener: KeyPadListener) {
        this.mListener = listener
    }
    /**
     * ----------- 버튼클릭 리스너 end -----------
     */

    fun setRefreshData() {
        keyPadData = BaseActivity.setKeyPadData()
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return keyPadData.size
    }

    override fun getItem(position: Int): Any {
        return keyPadData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        lateinit var holder: ViewHolder
        var view = convertView
        if (view == null) {
            holder = ViewHolder()
            view = LayoutInflater.from(mContext).inflate(R.layout.key_pad_item, parent, false)
            holder.itemBtn = view.findViewById(R.id.keypad_btn)
            holder.itemBack = view.findViewById(R.id.keypad_back)
            holder.itemRefresh = view.findViewById(R.id.keypad_refresh)

            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        // 숫자 키패드 텍스트 설정
        holder.itemBtn.text = keyPadData[position]

        if (holder.itemBtn.text == "왼") {
            // 하단 왼쪽 키패드: 없음
            holder.itemBtn.visibility = View.GONE
            holder.itemBack.visibility = View.GONE
            holder.itemRefresh.visibility = View.VISIBLE
        } else if (holder.itemBtn.text == "오") {
            // 하단 오른쪽 키패드: 지우기
            holder.itemBtn.visibility = View.GONE
            holder.itemBack.visibility = View.VISIBLE
            holder.itemRefresh.visibility = View.GONE
        }

        /**
         * 숫자 버튼 클릭 리스너
         */
        holder.itemBtn.setOnClickListener {
            mListener?.onNumClickEvent(holder.itemBtn.text.toString())
        }

        /**
         * 지움 버튼 클릭 리스너
         */
        holder.itemBack.setOnClickListener {
            mListener?.onEraserClickEvent()
        }

        /**
         * 새로고침 버튼 클릭 리스너
         */
        holder.itemRefresh.setOnClickListener {
            mListener?.onRefreshClickEvent()
            setRefreshData()
        }

        return view!!
    }

    inner class ViewHolder {
        lateinit var itemBtn: TextView
        lateinit var itemBack: RelativeLayout
        lateinit var itemRefresh: RelativeLayout
    }
}