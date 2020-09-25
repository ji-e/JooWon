package com.example.uohih.joowon.customview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.example.uohih.joowon.R
import com.example.uohih.joowon.ui.setting.SettingActivity
import com.example.uohih.joowon.ui.vacation.VacationActivity
import kotlinx.android.synthetic.main.view_top_title.view.*

/**
 * 타이틀 바
 *
 */
class TopTitleView : RelativeLayout, View.OnClickListener {

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
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mRootView = inflater.inflate(R.layout.view_top_title, null)
        addView(mRootView, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT))


        // Load attributes
        val at = context.obtainStyledAttributes(attrs, R.styleable.TopTitleView, defStyle, 0)


        // 상단 바 로고
        if (!at.hasValue(R.styleable.TopTitleView_btnLogo)) {
            top_btn_logo.visibility = View.GONE
        } else {
            top_tv_jw.visibility = View.VISIBLE
        }

        // 상단 바 뒤로가기
        if (!at.hasValue(R.styleable.TopTitleView_btnBack)) {
            top_btn_back.visibility = View.GONE
        } else {
            top_btn_back.setOnClickListener(mCloseBtnClickListener)
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

        // 상단 바 체크
        if (!at.hasValue(R.styleable.TopTitleView_btnCheck)) {
            top_btn_check.visibility = View.GONE
        }

        // 상단 바 휴가작성
        if (!at.hasValue(R.styleable.TopTitleView_btnWrite)) {
            top_btn_write.visibility = View.GONE
        } else {
            top_btn_write.setOnClickListener(this)
        }

        // 상단 바 환경설정
        if (!at.hasValue(R.styleable.TopTitleView_btnSetting)) {
            top_btn_setting.visibility = View.GONE
        } else {
            top_btn_setting.setOnClickListener(this)
        }


    }


    /**
     * 닫기 버튼
     */
    private val mCloseBtnClickListener: View.OnClickListener = OnClickListener {
        if (mContext is Activity) {
            (mContext as Activity).finish()

        }
    }


    fun setBackBtn() {
        top_btn_close.visibility = View.GONE
        top_btn_back.visibility = View.VISIBLE
        top_btn_back.setOnClickListener(mCloseBtnClickListener)
    }

    fun setCloseBtnClickListener(mCloseBtnClickListener: View.OnClickListener) {
        top_btn_close.setOnClickListener(mCloseBtnClickListener)
    }

    override fun onClick(v: View?) {
        when (v) {
            top_btn_write -> {
                val intent = Intent(mContext, VacationActivity::class.java)
                mContext.startActivity(intent)
            }
            top_btn_setting -> {
                val intent = Intent(mContext, SettingActivity::class.java)
                mContext.startActivity(intent)
            }
        }
    }

}