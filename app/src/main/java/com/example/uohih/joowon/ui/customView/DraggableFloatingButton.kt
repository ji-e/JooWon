package com.example.uohih.joowon.ui.customView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.RelativeLayout
import com.example.uohih.joowon.R
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class DraggableFloatingButton : RelativeLayout, OnTouchListener {

    private var downRawX = 0f
    private var downRawY = 0f
    private var dX = 0f
    private var dY = 0f


    interface DFBtnListener {
        fun onDFBtnDrag()
        fun onDFBBtnClick()
    }

    private var dfBtnListener: DFBtnListener? = null

    fun setDFBtnListener(dfBtnListener: DFBtnListener) {
        this.dfBtnListener = dfBtnListener
    }

    constructor(context: Context) : super(context) {
        init(null, 0, context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0, context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle, context)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int, mContext: Context) {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mRootView: View = inflater.inflate(R.layout.btn_plus, null)
        addView(mRootView, RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))


        setOnTouchListener(this)
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val action = motionEvent.action
        return if (action == MotionEvent.ACTION_DOWN) {
            downRawX = motionEvent.rawX
            downRawY = motionEvent.rawY
            dX = view.x - downRawX
            dY = view.y - downRawY
            true
        } else if (action == MotionEvent.ACTION_MOVE) {
            val viewWidth = view.width
            val viewHeight = view.height
            val viewParent = view.parent as View
            val parentWidth = viewParent.width
            val parentHeight = viewParent.height
            var newX = motionEvent.rawX + dX
            var newY = motionEvent.rawY + dY

            newX = max(0f, newX) // 왼쪽 끝을 넘지 않도록
            newY = max(0f, newY)  // 위쪽 끝을 넘지 않도록
            newX = min(parentWidth - viewWidth.toFloat(), newX) // 오른쪽 끝을 넘지 않도록
            newY = min(parentHeight - viewHeight.toFloat(), newY)  // 아래쪽 끝을 넘지 않도록
            view.animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(0)
                    .start()
            true
        } else if (action == MotionEvent.ACTION_UP) {
            val upRawX = motionEvent.rawX
            val upRawY = motionEvent.rawY
            val upDX = upRawX - downRawX
            val upDY = upRawY - downRawY
            if (abs(upDX) < CLICK_DRAG_TOLERANCE && abs(upDY) < CLICK_DRAG_TOLERANCE) {
                // 클릭 리스너
                dfBtnListener?.onDFBBtnClick()
//                performClick()
                true
            } else {
                // 드래그 리스너
                dfBtnListener?.onDFBtnDrag()
                true
            }
        } else {
            super.onTouchEvent(motionEvent)
        }
    }

    companion object {
        private const val CLICK_DRAG_TOLERANCE = 10f
    }
}
