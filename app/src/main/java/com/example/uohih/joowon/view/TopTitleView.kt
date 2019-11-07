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

        // 상단 바 체크
        if (!at.hasValue(R.styleable.TopTitleView_btnCheck)) {
            top_btn_check.visibility = View.GONE
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
     * 상단바 로고 -> 닫기
     */
    fun setClose() {
        top_btn_logo.setImageResource(R.drawable.btn_close_selector)
        top_btn_logo.setOnClickListener(mCloseBtnClickListener)

    }

    fun setCloseBtnClickListener(mCloseBtnClickListener: View.OnClickListener) {
        top_btn_logo.setOnClickListener(mCloseBtnClickListener)
    }
}