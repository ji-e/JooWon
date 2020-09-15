package com.example.uohih.joowon.view

import android.app.Dialog
import android.content.Context
import com.example.uohih.joowon.R

class CustomLoadingBar {
    companion object {
        private var mLoadingBar: Dialog? = null
        fun createCustomLoadingBarDialog(mContext: Context): Dialog {

            mLoadingBar = Dialog(mContext, R.style.Loading).apply {
                setCancelable(false)
                setContentView(R.layout.dialog_loading)
            }

            return mLoadingBar as Dialog
        }

        fun showLoadingBar(mContext: Context) {
            createCustomLoadingBarDialog(mContext)
            mLoadingBar?.show()
        }

        fun hideLoadingBar() {
            if (mLoadingBar != null) {
                try {
                    mLoadingBar?.dismiss()
                    mLoadingBar = null
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


}