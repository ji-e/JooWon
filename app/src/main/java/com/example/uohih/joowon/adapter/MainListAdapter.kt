package com.example.uohih.joowon.adapter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.main.PictureActivity
import com.example.uohih.joowon.view.CircularImageView
import com.example.uohih.joowon.worker.WorkerMainActivity
import java.util.*

/**
 * MainListActivity 아답터(직원 리스트)
 */
class MainListAdapter(private val mContext: Context, private val workerList: ArrayList<StaffData>) : BaseAdapter() {

    private var base = JWBaseApplication()
    // 체크 된 항목
    private var checked = ArrayList<Boolean>(count)

    /** ----------- 전체 선택 체크박스 리스너 start ----------- */

    private var mListener: mCheckboxListener? = null

    interface mCheckboxListener {
        fun onmClickEvent()
    }

    fun setmCheckboxListener(listener: mCheckboxListener) {
        this.mListener = listener
    }
    /** ----------- 전체 선택 체크박스 리스너 end ----------- */


    /**
     * 전체 선택 및 해제
     * isCheck: Boolean: 체크 상태 값
     */
    fun setAllCheckList(isCheck: Boolean) {
        checked.clear()
        for (i in 0 until count) {
            checked.add(isCheck)
        }
        notifyDataSetChanged()
    }

    /**
     * 체크 된 항목
     */
    fun check() {
        var array = ArrayList<String>()
        for (i in 0 until count) {
            if (checked[i]) {
                array.add(workerList[i].no.toString())
            }
        }
        notifyDataSetChanged()
        base.setDeleteItem(array)
    }


    /**
     * 데이터 삭제
     * position: Int: 삭제할 index
     */
    fun removeAt(position: Int) {
        workerList.removeAt(position)
        notifyDataSetChanged()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_view_main_list, parent, false)
        }


        val mLayoutItem = convertView?.findViewById<LinearLayout>(R.id.main_list_item)                   // 직원
        val mImgImage = convertView?.findViewById<CircularImageView>(R.id.main_list_img_people)     // 이미지
        val mTvName = convertView?.findViewById<TextView>(R.id.main_list_tv_name)                           // 이름
        val mTvJoinDate = convertView?.findViewById<TextView>(R.id.main_list_tv_join)                       // 입사 일
        val mTvPhoneNum = convertView?.findViewById<TextView>(R.id.main_list_tv_phone)                      // 핸드폰 번호
        val mTvVacationCnt = convertView?.findViewById<TextView>(R.id.main_list_tv_vacation)                // 휴가
        val mCheckbox = convertView?.findViewById<CheckBox>(R.id.main_list_check)                          // 체크
        val mImgNext = convertView?.findViewById<ImageView>(R.id.main_list_btn_next)                      // 다음 버튼


        // 이름
        mTvName?.text = workerList[position].name

        // 입사날짜
        val joinDate = (Constants.YYYYMMDD_PATTERN).toRegex().replace(workerList[position].joinDate.toString(), "$1-$2-$3")
        mTvJoinDate?.text = joinDate

        // 핸드폰 번호
        val phoneNum = (Constants.PHONE_NUM_PATTERN).toRegex().replace(workerList[position].phone, "$1-$2-$3")
        mTvPhoneNum?.text = phoneNum

        // 휴가
        mTvVacationCnt?.text = workerList[position].use.toString() + " / " + workerList[position].total.toString()

        // 사진
        if (!workerList[position].picture.isNullOrEmpty()) {
            var bitmap = BitmapFactory.decodeFile(workerList[position].picture)
            mImgImage?.setImageBitmap(bitmap)
        }
        mImgImage?.setOnClickListener {
            val intent = Intent(mContext, PictureActivity::class.java)
            intent.putExtra("picture", workerList[position].picture)
            mContext.startActivity(intent)
        }

        // 체크 초기값 세팅 (false)
        for (i in 0 until count) {
            checked.add(false)
        }

        // 체크박스 일 때
        if (mCheckbox != null) {
            if (mCheckbox.isChecked) {
                mCheckbox.isChecked = false
            } else {
                // 전체 선택 해제 리스너
                if (mListener != null) {
                    mListener?.onmClickEvent()
                }
                mCheckbox.isChecked = true
            }


            // 체크 박스 클릭 이벤트
            mCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                // 체크 상태값 변환
                checked[position] = isChecked
                // 전체 선택 해제 리스너
                if (!isChecked && mListener != null) {
                    mListener?.onmClickEvent()
                }
            }


            // 체크 상태 설정
            if (checked.isNotEmpty()) {
                mCheckbox.isChecked = checked[position]
            } else {
                mCheckbox.isChecked = false
            }
        }

        // 아이템 클릭 이벤트
        mLayoutItem?.setOnClickListener {
            val intent = Intent(mContext, WorkerMainActivity::class.java)

            val bundle = Bundle()
            bundle.putString("name", workerList[position].name)          //이름
            bundle.putString("phone", phoneNum)                          //핸드폰번호


            intent.putExtra("worker", bundle)
            mContext.startActivity(intent)
        }

        convertView?.tag = workerList[position]

        return convertView!!

    }


    override fun getItem(position: Int): Any? {
        return if (position >= count) null else workerList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return workerList.size
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
 * val picture: String: 사진
 */
class StaffData(val no: Int?, val name: String, val joinDate: Int, val phone: String, val use: Int, val total: Int, val picture: String?)