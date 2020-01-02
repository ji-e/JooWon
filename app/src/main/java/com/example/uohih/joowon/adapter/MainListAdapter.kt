package com.example.uohih.joowon.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import kotlinx.android.synthetic.main.list_item_main_list.view.*
import java.util.*

/**
 * MainListActivity 아답터(직원 리스트)
 */
class MainListAdapter( private val workerList: ArrayList<StaffData>) : RecyclerView.Adapter<MainListAdapter.ViewHolder>() {

    /*private var base = JWBaseApplication()
    // 체크 된 항목
    private var checked = ArrayList<Boolean>(count)

    *//** ----------- 전체 선택 체크박스 리스너 start ----------- *//*

    private var mListener: mCheckboxListener? = null

    interface mCheckboxListener {
        fun onmClickEvent()
    }

    fun setmCheckboxListener(listener: mCheckboxListener) {
        this.mListener = listener
    }
    *//** ----------- 전체 선택 체크박스 리스너 end ----------- *//*


    *//**
     * 전체 선택 및 해제
     * isCheck: Boolean: 체크 상태 값
     *//*
    fun setAllCheckList(isCheck: Boolean) {
        checked.clear()
        for (i in 0 until count) {
            checked.add(isCheck)
        }
        notifyDataSetChanged()
    }

    *//**
     * 체크 된 항목
     *//*
    fun check() {
        var array = ArrayList<String>()
        for (i in 0 until count) {
            if (checked[i]) {
                array.add(workerList[i].no.toString())
            }
        }
        notifyDataSetChanged()
        base.setDeleteItem(array)
    }*/


    /**
     * 데이터 삭제
     * position: Int: 삭제할 index
     */
    fun removeAt(position: Int) {
        workerList.removeAt(position)
        notifyDataSetChanged()
    }

    private val itemsOffset = IntArray(itemCount)

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        val mSwipeLayout = view.main_list_swipe_layout

        fun bind(listener: View.OnClickListener, item: StaffData) {
            view.main_list_tv_name.text = item.name
            view.main_list_tv_join.text = (Constants.YYYYMMDD_PATTERN).toRegex().replace(item.joinDate.toString(), "$1-$2-$3")
            view.main_list_tv_phone.text = (Constants.PHONE_NUM_PATTERN).toRegex().replace(item.phone, "$1-$2-$3")
            view.main_list_tv_vacation.append(item.use.toString())
            view.main_list_tv_vacation.append(" / ")
            view.main_list_tv_vacation.append(item.total.toString())

            if (!item.picture.isNullOrEmpty()) {
            val bitmap = BitmapFactory.decodeFile(item.picture)
                view.main_list_img_people.setImageBitmap(bitmap)
            }
            view.main_list_img_people.setOnClickListener(listener)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_main_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = workerList[position]
        val listener = View.OnClickListener {it ->

            Toast.makeText(it.context, "Clicked: ${item.name}", Toast.LENGTH_SHORT).show()
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item

        }

//        val onClick = View.OnClickListener { holder.mSwipeLayout.animateReset() }
//
//        holder.mLayoutLeft.isClickable = true
//        holder.mLayoutLeft.setOnClickListener(onClick)
//
//        holder.mLayoutRight.isClickable = true
//        holder.mLayoutRight.setOnClickListener(onClick)
//
//        holder.mSwipeLayout.setOnSwipeListener(object : SwipeLayout.OnSwipeListener {
//            override fun onBeginSwipe(swipeLayout: SwipeLayout, moveToRight: Boolean) {}
//
//            override fun onSwipeClampReached(swipeLayout: SwipeLayout, moveToRight: Boolean) {
//                //todo
//            }
//
//            override fun onLeftStickyEdge(swipeLayout: SwipeLayout, moveToRight: Boolean) {}
//
//            override fun onRightStickyEdge(swipeLayout: SwipeLayout, moveToRight: Boolean) {}
//        })
//
//        // 이름
//        holder.mTvName.text = workerList[position].name
//
//        // 입사날짜
//        val joinDate = (Constants.YYYYMMDD_PATTERN).toRegex().replace(workerList[position].joinDate.toString(), "$1-$2-$3")
//        holder.mTvJoinDate.text = joinDate
//
//        // 핸드폰 번호
//        val phoneNum = (Constants.PHONE_NUM_PATTERN).toRegex().replace(workerList[position].phone, "$1-$2-$3")
//        holder.mTvPhoneNum.text = phoneNum
//
//        // 휴가
//        holder.mTvVacationCnt.append(workerList[position].use.toString())
//        holder.mTvVacationCnt.append(" / ")
//        holder.mTvVacationCnt.append(workerList[position].total.toString())
//
//        // 사진
//        if (!workerList[position].picture.isNullOrEmpty()) {
//            var bitmap = BitmapFactory.decodeFile(workerList[position].picture)
//            holder.mImgImage.setImageBitmap(bitmap)
//        }
//        holder.mImgImage.setOnClickListener {
//            val intent = Intent(mContext, PictureActivity::class.java)
//            intent.putExtra("picture", workerList[position].picture)
//            mContext.startActivity(intent)
//        }
//
//
//        // 아이템 클릭 이벤트
//        holder.mLayoutItem.setOnClickListener {
//            val intent = Intent(mContext, WorkerMainActivity::class.java)
//
//            val bundle = Bundle()
//            bundle.putString("name", workerList[position].name)          //이름
//            bundle.putString("phone", phoneNum)                          //핸드폰번호
//
//
//            intent.putExtra("worker", bundle)
//            mContext.startActivity(intent)
//        }


    }

    override fun getItemCount(): Int {
        return workerList.size
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        if (holder.adapterPosition != RecyclerView.NO_POSITION) {
            itemsOffset[holder.adapterPosition] = holder.mSwipeLayout.offset
        }
    }


    /*override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_main_list, parent, false)
        }


        val mLayoutItem = convertView?.findViewById<LinearLayout>(R.id.main_list_item)                   // 직원
        val mImgImage = convertView?.findViewById<ImageView>(R.id.main_list_img_people)     // 이미지
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
    }*/




}
//class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView!!) {
////
////    val mLayoutItem: LinearLayout       // 직원
////    val mImgImage: ImageView            // 이미지
////    val mTvName: TextView               // 이름
////    val mTvJoinDate: TextView           // 입사 일
////    val mTvPhoneNum: TextView           // 핸드폰 번호
////    val mTvVacationCnt: TextView        // 휴가
////    val mLayoutLeft: FrameLayout        // 왼쪽 스와이프
////    val mLayoutRight: FrameLayout       // 오른쪽 스와이프
////    val mSwipeLayout: SwipeLayout       // 스와이프 레이아웃
//
//
////    init {
//    val mLayoutItem = itemView.main_list_item
//        val mImgImage = itemView.main_list_img_people
//        val mTvName = itemView.main_list_tv_name
//        val mTvJoinDate = itemView.main_list_tv_join
//        val mTvPhoneNum = itemView.main_list_tv_phone
//        val mTvVacationCnt = itemView.main_list_tv_vacation
//        val mLayoutLeft = itemView.main_list_left_view
//        val mLayoutRight = itemView.main_list_right_view
//        val mSwipeLayout = itemView.main_list_swipe_layout
////    }
//
//}


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
class StaffData(
        val no: Int?,
        val name: String,
        val joinDate: Int,
        val phone: String,
        val use: Int,
        val total: Int,
        val picture: String?)