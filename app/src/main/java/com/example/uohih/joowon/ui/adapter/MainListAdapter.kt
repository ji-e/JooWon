package com.example.uohih.joowon.ui.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.databinding.ListItemMainListBinding
import com.example.uohih.joowon.model.JW3001
import com.example.uohih.joowon.ui.main.MainListItemViewModel
import kotlinx.android.synthetic.main.list_item_main_list.view.*
import ru.rambler.libs.swipe_layout.SwipeLayout
import java.io.IOException


/**
 * MainListActivity 아답터(직원 리스트)
 */
class MainListAdapter : RecyclerView.Adapter<BindingViewHolder<ListItemMainListBinding>>() {

    /*private var base = JWBaseApplication()
    // 체크 된 항목
    private var checked = ArrayList<Boolean>(count)

    */
    /** ----------- 전체 선택 체크박스 리스너 start ----------- *//*

    private var mListener: mCheckboxListener? = null

    interface mCheckboxListener {
        fun onmClickEvent()
    }

    fun setmCheckboxListener(listener: mCheckboxListener) {
        this.mListener = listener
    }
    */
    /** ----------- 전체 선택 체크박스 리스너 end ----------- *//*


    */
    /**
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

    */
    /**
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
        workerList.removeAt(position)
        notifyDataSetChanged()
    }

    private val itemsOffset = IntArray(itemCount)
    private lateinit var mContext: Context

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        val mSwipeLayout: SwipeLayout = view.mainList_swipeLayout
        val mLayoutLeft = itemView.mainList_leftView      //왼쪽 스와이프
        val mLayoutRight = itemView.mainList_rightView    //오른쪽 스와이프

        val mTvName = view.mainList_tvName                //이름
        val mTvJoin = view.mainList_tvJoin                //입사날짜
        val mTvPhone = view.mainList_tvPhone              //핸드폰번호
        val mTvVacation = view.mainList_tvVacation        //휴가
        val mImgPeople = view.mainList_imgPeople          //프로필 사진


        fun bind(listener: View.OnClickListener, item: JW3001.resbodyLst, mContext: Context) {
            mTvName.text = item.name
            mTvJoin.text = (Constants.YYYYMMDD_PATTERN).toRegex().replace(item.entered_date.toString(), "$1-$2-$3")
            mTvPhone.text = (Constants.PHONE_NUM_PATTERN).toRegex().replace(item.phone_number.toString(), "$1-$2-$3")
            val total_vacation_cnt = if (item.total_vacation_cnt != null) item.total_vacation_cnt.toInt() else 0
            val remain_vacation_cnt = if (item.remain_vacation_cnt != null) item.remain_vacation_cnt.toInt() else 0
            val use_vacation_cnt = (total_vacation_cnt - remain_vacation_cnt)
            mTvVacation.append(use_vacation_cnt.toString())
            mTvVacation.append(" / ")
            mTvVacation.append(total_vacation_cnt.toString())

            if (!item.profile_image.isNullOrEmpty()) {
                val bitmap = BitmapFactory.decodeFile(item.profile_image)
                lateinit var exif: ExifInterface

                try {
                    exif = ExifInterface(item.profile_image)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val exifOrientation: Int
                val exifDegree: Int

                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                exifDegree = JWBaseActivity().exifOrientationToDegrees(exifOrientation)

//                mImgPeople.setImageBitmap(bitmap)
                Glide.with(mContext).load(JWBaseActivity().rotate(bitmap, exifDegree.toFloat())).apply(RequestOptions().circleCrop()).into(mImgPeople)
            }
            mImgPeople.setOnClickListener(listener)
            view.mainList_item.setOnClickListener(listener)

            mLayoutLeft.isClickable = true
            mLayoutLeft.setOnClickListener(listener)

            mLayoutRight.isClickable = true
            mLayoutRight.setOnClickListener(listener)

        }
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        mContext = parent.context
//        val itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_main_list, parent, false)
//        return ViewHolder(itemView)
//    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = workerList[position]
//        val listener = View.OnClickListener {
//            when (it.id) {
//                R.id.mainList_imgPeople -> { //프로필사진
//                    val intent = Intent(mContext, PictureActivity::class.java)
//                    intent.putExtra("picture", workerList[position].profile_image)
//                    mContext.startActivity(intent)
//                }
//                R.id.mainList_item -> { //리스트 아이템
//
//                    val bundle = Bundle()
//                    bundle.putString("name", holder.mTvName.text.toString())          //이름
//                    bundle.putString("phone", holder.mTvPhone.text.toString())        //핸드폰번호
//
//                    mListener?.onmClickEvent(bundle)
////                    val intent = Intent(mContext, WorkerMainActivity::class.java)
////                    intent.putExtra("worker", bundle)
////                    mContext.startActivity(intent)
//                }
//                R.id.mainList_leftView -> { //왼쪽 스와이프 (즐겨찾기)
//                    holder.mSwipeLayout.animateReset()
//                }
//                R.id.mainList_rightView -> { //오른쪽 스와이프 (삭제)
//                    holder.mSwipeLayout.animateReset()
//                    val dbHelper = DBHelper(mContext)
//                    val customDialog = CustomDialog(mContext, android.R.style.Theme_Material_Dialog_MinWidth)
//                    customDialog.showDialog(mContext, String.format(mContext.resources.getString(R.string.workerUpdate_delete_msg),
//                            holder.mTvName.text.toString()),
//                            mContext.resources.getString(R.string.btnCancel), null,
//                            mContext.resources.getString(R.string.btnConfirm), DialogInterface.OnClickListener { dialog, which ->
//                        dbHelper.delete(dbHelper.tableNameWorkerJW, workerList[position]._id.toString())
//
//
//                        val intent = Intent(mContext, MainListActivity::class.java)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                        mContext.startActivity(intent)
//
//                    })
//
//                }
//            }
//        }
//
//
//        holder.apply {
//            item.let { bind(listener, it, mContext) }
//            itemView.tag = item
//        }
//
//    }

    override fun getItemCount(): Int {
        return workerList.size
    }

//    override fun onViewDetachedFromWindow(holder: ViewHolder) {
//        if (holder.adapterPosition != RecyclerView.NO_POSITION) {
//            itemsOffset[holder.adapterPosition] = holder.mSwipeLayout.offset
//        }
//        holder.mSwipeLayout.animateReset()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ListItemMainListBinding> {
        val inflater = LayoutInflater.from(parent.context)
        return BindingViewHolder<ListItemMainListBinding>(inflater.inflate(R.layout.list_item_main_list, parent, false))
    }

    private var listItemMainViewModel: MutableList<MainListItemViewModel>? = null

    override fun onBindViewHolder(holder: BindingViewHolder<ListItemMainListBinding>, position: Int) {
        var c = listItemMainViewModel?.get(position)
        holder.binding.
    }


}
