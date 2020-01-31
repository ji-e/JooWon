import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.base.SizeConverter

class VacationAdapter(mContext: Context, private val list: List<String>) : BaseAdapter() {
    private val mContext = mContext
    private lateinit var viewHolder: ViewHolder

    /** 아이템 높이 리스너 ------*/
    private lateinit var getItemHeightListener: GetItemHeightListener

    interface GetItemHeightListener {
        fun getItemHeight(height: Int)
    }

    fun setGetItemHeightListener(getItemHeightListener: GetItemHeightListener) {
        this.getItemHeightListener = getItemHeightListener
    }

    private lateinit var setVacationCnt: ArrayList<String>
    private var setCheckBox= ArrayList<Boolean>(count)

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            viewHolder = ViewHolder()
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_vacation, viewGroup, false)

            viewHolder.mLayout = convertView.findViewById(R.id.item_vacation_layout)
            viewHolder.mDate = convertView.findViewById(R.id.item_vacation_date)
            viewHolder.mCnt = convertView.findViewById(R.id.item_vacation_cnt)
            viewHolder.mCheck = convertView.findViewById(R.id.item_vacation_check)


            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }


//        for(i in 0 until count){
//            setCheckBox[i] = false
//        }

        viewHolder.mDate.text = list[position]

        viewHolder.mCheck.setOnCheckedChangeListener { compoundButton, b ->
           setCheckBox[position] = b
        }

        if(setCheckBox[position])viewHolder.mCnt.text = "0.5"
        else viewHolder.mCnt.text = "1.0"

        viewHolder.mLayout.viewTreeObserver.addOnGlobalLayoutListener {
            var height = viewHolder.mLayout.height
            getItemHeightListener.getItemHeight((height + 2) * count)
        }

        convertView?.tag = viewHolder
        return convertView!!
    }

    internal inner class ViewHolder {
        lateinit var mLayout: LinearLayout
        lateinit var mDate: TextView    // 휴가 날짜
        lateinit var mCnt: TextView     // 휴가 개수
        lateinit var mCheck: CheckBox
    }

}
