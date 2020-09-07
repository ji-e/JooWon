import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.LogUtil

class VacationAdapter(mContext: Context, private val list: List<String>) : BaseAdapter() {
    private val mContext = mContext
    private lateinit var viewHolder: ViewHolder


    /** 휴가 등록 리스너 ------*/
    private lateinit var vacationAdapterListener: VacationAdapterListener

    interface VacationAdapterListener {
        fun getItemHeight(height: Int)
        fun getVacationCnt(mCheckBoxList: ArrayList<Boolean>, cnt: Double)
    }

    fun setVacationAdapterListener(vacationAdapterListener: VacationAdapterListener) {
        this.vacationAdapterListener = vacationAdapterListener
    }


    // 반차 체크박스 리스트
    private var mCheckBoxList = arrayListOf<Boolean>()

    /**
     * 휴가 개수 가져오기
     */
    private fun getVacationCnt(): Double {
        var cnt = 0.0
        for (i in 0 until count) {
            if (mCheckBoxList[i]) cnt += 0.5
            else cnt ++
        }
        return cnt
    }

    /**
     * 휴가 체크박스 리스트 가져오기
     */
    fun getCheckBoxList(): ArrayList<Boolean> {
        return mCheckBoxList
    }



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


        viewHolder.mDate.text = list[position]
        viewHolder.mCheck.isChecked = mCheckBoxList[position]
        if(mCheckBoxList[position])viewHolder.mCnt.text = "0.5"
        else viewHolder.mCnt.text = "1.0"

        viewHolder.mCheck.setOnCheckedChangeListener { compoundButton, b ->
            mCheckBoxList[position] = b
            vacationAdapterListener.getVacationCnt(getCheckBoxList(), getVacationCnt())
            notifyDataSetChanged()
        }

        viewHolder.mLayout.viewTreeObserver.addOnGlobalLayoutListener {
            var height = viewHolder.mLayout.height
            vacationAdapterListener.getItemHeight((height + 2) * count)
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

    init {
        mCheckBoxList.clear()
        for (i in 0 until count) {
            mCheckBoxList.add(false)
        }

    }

}
