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
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.util.SizeConverterUtil
import kotlinx.android.synthetic.main.btn_negative_bottom.view.*
import kotlinx.android.synthetic.main.btn_positive_bottom.view.*
import kotlinx.android.synthetic.main.dialog_basic.view.*

/**
 * 다이얼로그
 * context: Context
 * theme: Int
 */
class CustomDialog(mContext: Context) : BaseBottomDialog(mContext), View.OnClickListener {


    private var binding: DialogBasicBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_basic, null, false)

    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var btnClose: ImageButton
    private lateinit var btnNo: Button
    private lateinit var btnYes: Button

    private var mCloseBtnClickListener: View.OnClickListener? = null
    private var mNoBtnClickListener: View.OnClickListener? = null
    private var mYesBtnClickListener: View.OnClickListener? = null

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
        btnNo = binding.dialogBtnNo.btnNegative
        btnYes = binding.dialogBtnYes.btnPositive

        btnClose.setOnClickListener(this)
        btnNo.setOnClickListener(this)
        btnYes.setOnClickListener(this)
    }


    fun setBottomDialog(strContent: String,
                        strYes: String, onYesListener: View.OnClickListener?) {

        setBottomDialog(mContext.getString(R.string.dialog_title), strContent, null, null, null, strYes, onYesListener)
    }


    fun setBottomDialog(strTitle: String,
                        strContent: String,
                        onCloseListener: View.OnClickListener?,
                        strYes: String, onYesListener: View.OnClickListener?) {

        setBottomDialog(strTitle, strContent, onCloseListener, null, null, strYes, onYesListener)
    }

    fun setBottomDialog(strTitle: String,
                        strContent: String,
                        onCloseListener: View.OnClickListener?,
                        strNo: String?, onNoListener: View.OnClickListener?,
                        strYes: String, onYesListener: View.OnClickListener?) {

        LogUtil.e(onYesListener.toString())

        onCloseListener?.let { mCloseBtnClickListener = it }
//        onNoListener?.let { mNoBtnClickListener = it }
//        onYesListener?.let { mYesBtnClickListener = it }

        onNoListener?.let {
            btnNo.setOnClickListener(it)
        }
        onYesListener?.let {
            btnYes.setOnClickListener(it)
        }

        tvTitle.text = strTitle
        tvContent.text = strContent
        btnNo.text = strNo
        btnYes.text = strYes

        if(strNo.isNullOrEmpty()) {
            binding.dialogBtnNo.visibility = View.GONE
        }


        // 다이얼로그 높이 설정
        setPeekHeight(250f)
    }

    override fun onClick(view: View) {
        when (view) {
            btnClose -> {
                // 닫기버튼
                mCloseBtnClickListener?.let {
                    btnClose.setOnClickListener(it)
                }
                dismiss()
            }
            btnNo,  btnYes -> {
//                mNoBtnClickListener?.let {
//                    btnNo.setOnClickListener(it)
//                }
                dismiss()
            }
        }

    }
//    class Builder(private val context: Context) {
//        private var dialogText: String? = null
//        private var dialogBtnNoText: String? = null
//        private var dialogBtnYesText: String? = null
//
//        private var mNoBtnClickListener: DialogInterface.OnClickListener? = null
//        private var mYesBtnClickListener: DialogInterface.OnClickListener? = null
//
//        /**
//         * 메세지 세팅
//         */
//        fun setMsg(text: String): Builder {
//            dialogText = text
//            return this
//        }
//
//        /**
//         * 취소 버튼 리스너
//         */
//        fun setNoBtnClickListener(text: String?, mNoBtnClickListener: DialogInterface.OnClickListener?): Builder {
//            dialogBtnNoText = text
//            this.mNoBtnClickListener = mNoBtnClickListener
//            return this
//        }
//
//        /**
//         * 확인 버튼 리스너
//         */
//        fun setYesBtnClickListener(text: String, mYesBtnClickListener: DialogInterface.OnClickListener?): Builder {
//            dialogBtnYesText = text
//            this.mYesBtnClickListener = mYesBtnClickListener
//            return this
//        }
//
//        /**
//         * 커스텀 다이얼로그 생성
//         */
//        fun create(): CustomDialog {
//            val dialog = CustomDialog(context)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            val contentView = LayoutInflater.from(context).inflate(R.layout.dialog_basic, null)
//
//            dialog.addContentView(contentView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
//
//
//            // 팝업 메세지 세팅
//            if (!dialogText.isNullOrEmpty()) {
//                contentView.dialog_text.text = dialogText
//            }
//
//            // 취소 버튼
//            if (dialogBtnNoText.isNullOrEmpty()) {
//                contentView.dialog_no.visibility = View.GONE
//            } else {
//                contentView.dialog_no.text = dialogBtnNoText
//                if (mNoBtnClickListener != null) {
//                    contentView.dialog_no.setOnClickListener {
//                        mNoBtnClickListener!!.onClick(dialog, DialogInterface.BUTTON_NEGATIVE)
//                        dialog.dismiss()
//                    }
//                } else {
//                    contentView.dialog_no.setOnClickListener {
//                        dialog.dismiss()
//                    }
//                }
//
//            }
//
//            // 확인 버튼
//            if (dialogBtnYesText.isNullOrEmpty()) {
//                contentView.dialog_yes.visibility = View.GONE
//            } else {
//                contentView.dialog_yes.text = dialogBtnYesText
//                if (mYesBtnClickListener != null) {
//                    contentView.dialog_yes.setOnClickListener {
//                        mYesBtnClickListener!!.onClick(dialog, DialogInterface.BUTTON_POSITIVE)
//                        dialog.dismiss()
//                    }
//                } else {
//                    contentView.dialog_yes.setOnClickListener {
//                        dialog.dismiss()
//                    }
//                }
//
//            }
//
//            return dialog
//        }
//
//    }


//    /**
//     * 확인 버튼 다이얼로그
//     */
//    fun showDialog(context: Context?, msg: String, yesBtnTxt: String, yesBtnListener: DialogInterface.OnClickListener?) {
//        showDialog(context, msg, null, null, yesBtnTxt, yesBtnListener)
//    }
//
//    /**
//     * 취소, 확인 버튼 다이얼로그
//     */
//    fun showDialog(context: Context?, msg: String, pnoBtnTxt: String?, noBtnListener: DialogInterface.OnClickListener?, yesBtnTxt: String, yesBtnListener: DialogInterface.OnClickListener?): CustomDialog? {
//        if (context == null) {
//            return null
//        }
//
//        if (context is Activity) {
//            val activity = context as Activity?
//
//            if (activity!!.isFinishing || activity.isDestroyed) {
//                return null
//            }
//        }
//
//        val builder = Builder(context)
//        builder.setMsg(msg)
//        builder.setNoBtnClickListener(pnoBtnTxt, noBtnListener)
//        builder.setYesBtnClickListener(yesBtnTxt, yesBtnListener)
//        val dialog = builder.create()
//        dialog.show()
//        return dialog
//    }
}