package com.example.uohih.joowon.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.uohih.joowon.R
import java.util.*

/**
 * MainListActivity 아답터(직원 리스트)
 */
class MainListAdapter (private val mContext: Context, private val dailyList: ArrayList<StaffData>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_view_main_list, parent, false)
        }

        // 직원
        val item = convertView?.findViewById<LinearLayout>(R.id.main_list_item)
        // 이미지
        val image = convertView?.findViewById<ImageView>(R.id.main_list_img_people)
        // 이름
        val name = convertView?.findViewById<TextView>(R.id.main_list_tv_name)
        // 입사 일
        val join = convertView?.findViewById<TextView>(R.id.main_list_tv_join)
        // 핸드폰 번호
        val phone = convertView?.findViewById<TextView>(R.id.main_list_tv_phone)
        // 휴가
        val vacation = convertView?.findViewById<TextView>(R.id.main_list_tv_vacation)


        if (name != null) {
            name.text= dailyList[position].name
        }
        if (join != null) {
            join.text= dailyList[position].joinDate.toString()
        }
        if (phone != null) {
            phone.text= dailyList[position].phone
        }
        if (vacation != null) {
            vacation.text= dailyList[position].use.toString()+" / "+dailyList[position].total.toString()
        }








        convertView?.tag = dailyList[position]

        return convertView!!

    }

    override fun getItem(position: Int): Any? {
        return if (position >= count) null else dailyList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dailyList.size
    }
}


/**
 * 직원 정보 데이타
 * val no: Int: 고유번호
 * val name: String: 이름
 * val joinDate: Int: 입사 날짜
 * val phone: String: 핸드폰 번호
 * val use: Int: 사용 휴가 갯수
 * val total: Int: 전체 휴가 갯수
 */
class StaffData(val no: Int?, val name: String, val joinDate:Int, val phone: String, val use:Int, val total:Int)