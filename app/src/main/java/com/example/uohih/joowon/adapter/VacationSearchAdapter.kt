
import android.content.Context
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.uohih.joowon.R
import com.example.uohih.joowon.adapter.StaffData
import com.example.uohih.joowon.base.JWBaseActivity
import java.io.IOException

class VacationSearchAdapter(mContext: Context, private val list: List<StaffData>) : BaseAdapter() {
    private val mContext = mContext
    private lateinit var viewHolder: ViewHolder

    interface VacationSearchListener {
        fun onClickItem(position: Int)
    }

    lateinit var mVacationSearchListener: VacationSearchListener

    fun setmVacationSearchListener(mListener: VacationSearchListener){
        mVacationSearchListener = mListener
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
        var convertView= convertView
        if (convertView == null) {
            viewHolder = ViewHolder()
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_vacation, viewGroup, false)

            viewHolder.mLayout = convertView.findViewById(R.id.item_vacation_layout)
            viewHolder.mImg = convertView.findViewById(R.id.item_vacation_img)
            viewHolder.mName = convertView.findViewById(R.id.item_vacation_name)
            viewHolder.mPhone = convertView.findViewById(R.id.item_vacation_phone)


            convertView.tag=viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        if (!list[position].picture.isNullOrEmpty()) {
            val bitmap = BitmapFactory.decodeFile(list[position].picture)
            lateinit var exif: ExifInterface

            try {
                exif = ExifInterface(list[position].picture)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val exifOrientation: Int
            val exifDegree: Int

            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            exifDegree = JWBaseActivity().exifOrientationToDegrees(exifOrientation)

            Glide.with(mContext).load(JWBaseActivity().rotate(bitmap, exifDegree.toFloat())).apply(RequestOptions().circleCrop()).into(viewHolder.mImg)
        }

        viewHolder.mName.text = list[position].name
        viewHolder.mPhone.text = list[position].phone

        // 아이템 클릭 리스너
        viewHolder.mLayout.setOnClickListener {
            mVacationSearchListener.onClickItem(position)
        }

        convertView?.tag = viewHolder
        return convertView!!
    }

    internal inner class ViewHolder {
        lateinit var mLayout: LinearLayout
        lateinit var mImg: ImageView    // 프로필 사진
        lateinit var mName: TextView    // 이름
        lateinit var mPhone: TextView   // 핸드폰 번호
    }
}