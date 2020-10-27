package com.example.uohih.joowon.ui.customView

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
            top_btnLogo.visibility = View.GONE
        } else {
            top_tvJW.visibility = View.VISIBLE
        }

        // 상단 바 뒤로가기
        if (!at.hasValue(R.styleable.TopTitleView_btnBack)) {
            top_btnBack.visibility = View.GONE
        } else {
            top_btnBack.setOnClickListener(mCloseBtnClickListener)
        }

        // 상단 바 닫기
        if (!at.hasValue(R.styleable.TopTitleView_btnClose)) {
            top_btnClose.visibility = View.GONE
        } else {
            top_btnClose.setOnClickListener(mCloseBtnClickListener)
        }


        // 상단 바 타이틀
        if (!at.hasValue(R.styleable.TopTitleView_tvTitle)) {
            top_tvTitle.visibility = View.GONE
        } else {
            top_tvTitle.text = at.getText(R.styleable.TopTitleView_tvTitle)
        }

        // 상단 바 체크
        if (!at.hasValue(R.styleable.TopTitleView_btnCheck)) {
            top_ckb.visibility = View.GONE
        }

        // 상단 바 휴가작성
        if (!at.hasValue(R.styleable.TopTitleView_btnWrite)) {
            top_btnWrite.visibility = View.GONE
        } else {
            top_btnWrite.setOnClickListener(this)
        }

        // 상단 바 환경설정
        if (!at.hasValue(R.styleable.TopTitleView_btnSetting)) {
            top_btnSetting.visibility = View.GONE
        } else {
            top_btnSetting.setOnClickListener(this)
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
        top_btnClose.visibility = View.GONE
        top_btnBack.visibility = View.VISIBLE
        top_btnBack.setOnClickListener(mCloseBtnClickListener)
    }

    fun setCloseBtnClickListener(mCloseBtnClickListener: View.OnClickListener) {
        top_btnClose.setOnClickListener(mCloseBtnClickListener)
    }

    override fun onClick(v: View?) {
        when (v) {
            top_btnWrite -> {
                val intent = Intent(mContext, VacationActivity::class.java)
                mContext.startActivity(intent)
            }
            top_btnSetting -> {
                val intent = Intent(mContext, SettingActivity::class.java)
                mContext.startActivity(intent)
            }
        }
    }

}