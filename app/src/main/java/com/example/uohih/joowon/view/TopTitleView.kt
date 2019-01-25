package com.example.uohih.joowon.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.example.uohih.joowon.R
import kotlinx.android.synthetic.main.view_top_title.view.*

/**
 * 타이틀 바
 *
 */
class TopTitleView : RelativeLayout {
    private lateinit var mContext: Context
    private lateinit var mRootView: View

    constructor(context: Context) : super(context) {
        init(null, 0, context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0, context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle, context)
    }


    /**
     * 초기화
     */
    private fun init(attrs: AttributeSet?, defStyle: Int, mContext: Context) {
        this.mContext = mContext
        val inflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mRootView = inflater.inflate(R.layout.view_top_title, null)
        addView(mRootView, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT))


        // Load attributes
        val at = context.obtainStyledAttributes(attrs, R.styleable.TopTitleView, defStyle, 0)


        // 상단 바 로고
        if (!at.hasValue(R.styleable.TopTitleView_btnLogo)) {
            top_btn_logo.visibility = View.GONE
        }

        // 상단 바 닫기
        if (!at.hasValue(R.styleable.TopTitleView_btnClose)) {
            top_btn_close.visibility = View.GONE
        } else {
            top_btn_close.setOnClickListener(mCloseBtnClickListener)
        }


        // 상단 바 타이틀
        if (!at.hasValue(R.styleable.TopTitleView_tvTitle)) {
            top_tv_title.visibility = View.GONE
        } else {
            top_tv_title.text = at.getText(R.styleable.TopTitleView_tvTitle)
        }


        // 상단 바 연필
        if (!at.hasValue(R.styleable.TopTitleView_btnWrite)) {
            top_btn_write.visibility = View.GONE
        }


        // 상단 바 캘린더
        if (!at.hasValue(R.styleable.TopTitleView_btnCalendar)) {
            top_btn_calendar.visibility = View.GONE
        }

        // 상단 바 리스트
        if (!at.hasValue(R.styleable.TopTitleView_btnList)) {
            top_btn_list.visibility = View.GONE
        }

        // 상단 바 세팅
        if (!at.hasValue(R.styleable.TopTitleView_btnSetting)) {
            top_btn_setting.visibility = View.GONE
        } else {
            top_btn_setting.setOnClickListener(mSettingBtnClickListener)
        }
    }


    /**
     * 닫기 버튼
     */
    private val mCloseBtnClickListener: View.OnClickListener = OnClickListener {
        if (mContext != null && mContext is Activity) {
            (mContext as Activity).finish()
        }
    }

    /**
     * 연필 버튼
     */
    fun setWriteBtnClickListener(mWriteBtnClickListener: View.OnClickListener) {
        top_btn_write.setOnClickListener(mWriteBtnClickListener)
    }

    /**
     * 캘린더 버튼
     */
    fun setCalendarBtnClickListener(mCalendarBtnClickListener: View.OnClickListener) {
        top_btn_calendar.setOnClickListener(mCalendarBtnClickListener)
    }

    /**
     * 리스트 버튼
     */
    fun setListBtnClickListener(mListBtnClickListener: View.OnClickListener) {
        top_btn_list.setOnClickListener(mListBtnClickListener)
    }

    /**
     * 세팅 버튼
     */
    private var mSettingBtnClickListener: View.OnClickListener = OnClickListener {

    }
}