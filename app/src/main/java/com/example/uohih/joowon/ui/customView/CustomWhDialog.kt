package com.example.uohih.joowon.ui.customView

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.example.uohih.joowon.R
import com.example.uohih.joowon.databinding.DialogBasicBinding
import com.example.uohih.joowon.databinding.DialogWhBinding
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.util.SizeConverterUtil
import kotlinx.android.synthetic.main.btn_negative_bottom.view.*
import kotlinx.android.synthetic.main.btn_positive_bottom.view.*
import kotlinx.android.synthetic.main.btn_white.view.*
import kotlinx.android.synthetic.main.dialog_basic.view.*

/**
 * 다이얼로그
 * context: Context
 * theme: Int
 */
class CustomWhDialog(mContext: Context) : BaseBottomDialog(mContext), View.OnClickListener {


    private var binding: DialogWhBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_wh, null, false)

    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var btnClose: ImageButton
    private lateinit var btn1: Button
    private lateinit var btn2: Button

    init {
        binding.run {
            lifecycleOwner = mContext as LifecycleOwner
            setContentView(binding.root)
        }
        initView()
    }

    fun initView() {
        tvTitle = binding.dialogTvTitle
        tvContent = binding.dialogTvContent
        btnClose = binding.dialogBtnClose
        btn1 = binding.dialogBtn1.btnWhite
        btn2 = binding.dialogBtn2.btnWhite

        btnClose.setOnClickListener(this)
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
    }


    fun setBottomDialog(strContent: String?,
                        strYes: String, onYesListener: View.OnClickListener?) {

        setBottomDialog(mContext.getString(R.string.dialog_title), strContent, null, null, null, strYes, onYesListener)
    }


    fun setBottomDialog(strTitle: String,
                        strContent: String?,
                        onCloseListener: View.OnClickListener?,
                        strYes: String, onYesListener: View.OnClickListener?) {

        setBottomDialog(strTitle, strContent, onCloseListener, null, null, strYes, onYesListener)
    }

    fun setBottomDialog(strTitle: String,
                        strContent: String?,
                        onCloseListener: View.OnClickListener?,
                        strNo: String?, onBtn1Listener: View.OnClickListener?,
                        strYes: String, onBtn2Listener: View.OnClickListener?) {

        onCloseListener?.let { btnClose.setOnClickListener(it) }
        onBtn1Listener?.let { btn1.setOnClickListener(it) }
        onBtn2Listener?.let { btn2.setOnClickListener(it) }

        tvTitle.text = strTitle
        tvContent.text = strContent
        btn1.text = strNo
        btn2.text = strYes


        if (onCloseListener == null) {
            btnClose.visibility = View.GONE
            setCancelable(false)
        }

        if (strContent.isNullOrEmpty()) {
            tvContent.visibility = View.GONE
        }


        // 다이얼로그 높이 설정
        setPeekHeight(200f)
    }

    override fun onClick(view: View) {
        when (view) {
            btnClose, btn1, btn2 -> {
                dismiss()
            }
        }
    }
}