package com.example.uohih.joowon.ui.customView

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.example.uohih.joowon.R
import com.example.uohih.joowon.util.SizeConverterUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


open class BaseBottomDialog : BottomSheetDialog {
    var mContext:Context
    val mWindow = this.window
    private var mAnimation = -1
    private var mResId = -1
    private var mPeekHeight = 0

    constructor(mContext: Context) : super(mContext) {
        this.mContext = mContext
    }

    constructor(mContext: Context, theme: Int) : super(mContext, theme) {
       this.mContext = mContext
    }

    /**
     * BottomSheetBehavior.STATE_COLLAPSED : height 만큼 보임.
     * BottomSheetBehavior.STATE_EXPANDED  : 가득 차게 보임.
     * BottomSheetBehavior.STATE_HIDDEN    : 숨김 처리.
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setOnShowListener(object : DialogInterface.OnShowListener {
            override fun onShow(dialog: DialogInterface?) {
                val bottomSheetDialog = dialog as BottomSheetDialog
                val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)

                bottomSheet?.background =
                        if (mResId == -1) ContextCompat.getDrawable(mContext, R.drawable.rectangle_r4_ffffff)
                        else ContextCompat.getDrawable(mContext, mResId)

                val coordinatorLayout = bottomSheet?.parent as CoordinatorLayout
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                coordinatorLayout.isFocusableInTouchMode = false
                if (mPeekHeight == 0) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
                } else {
                    bottomSheet.layoutParams.height = mPeekHeight
                    bottomSheetBehavior.peekHeight = mPeekHeight
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    if (mWindow != null) {
                        mWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        mWindow.setDimAmount(0.6f) // #99000000

                        mWindow.attributes.windowAnimations =
                                if (mAnimation == -1) R.style.DefaultBottomDialog
                                else mAnimation

                    }

                    bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(@NonNull view: View, i: Int) {
                            if (i == BottomSheetBehavior.STATE_DRAGGING) {
                                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                            }
                        }

                        override fun onSlide(@NonNull view: View, v: Float) {

                            if (v == -1.0f) {
                                dismiss()
                            }
                        }
                    })

                    coordinatorLayout.parent.requestLayout()
                }
            }

        })

//        val root = findViewById<ViewGroup>(android.R.id.content)
//        setCancelable(false)

    }

    protected fun setAnimation(animation: Int) {
        mAnimation = animation
    }

    protected fun setPeekHeight(peekHeight: Float) {
        mPeekHeight = SizeConverterUtil(mContext).dp(peekHeight)
    }

    protected fun setBackground(resId: Int) {
        mResId = resId
    }

    private val MIN_CLICK_INTERVAL: Long = 600
    private var mLastClickTime: Long = 0
//    fun onSingleClick(): Boolean {
//        val currentClickTime = SystemClock.uptimeMillis()
//        val elapsedTime = currentClickTime - mLastClickTime
//        mLastClickTime = currentClickTime
//        return elapsedTime > MIN_CLICK_INTERVAL
//    }

}
