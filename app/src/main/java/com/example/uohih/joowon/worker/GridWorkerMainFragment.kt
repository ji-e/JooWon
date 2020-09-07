package com.example.uohih.joowon.worker

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.uohih.joowon.R
import com.example.uohih.joowon.adapter.CalendarAdapter
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.database.VacationData
import kotlinx.android.synthetic.main.fragment_grid_worker_main.*
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class GridWorkerMainFragment : Fragment() {
    private lateinit var mContext: Context
    private lateinit var calendarAdapter: CalendarAdapter

    private var vacationList = arrayListOf<VacationData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this.activity!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_grid_worker_main, container, false)
        return root
    }

    // 뷰 생성이 완료되면 호출되는 메소드

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        section_label.text=arguments?.let{
//            it.getInt(num).toString()
//        }
        setCalendarView(LocalDate.now(), LocalDate.now())
        grid_worker.adapter = calendarAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance(): GridWorkerMainFragment {
            return GridWorkerMainFragment().apply {
                arguments = Bundle().apply {
                    //                    putInt(num, Number)
                }
            }
        }

        fun newInstance(v: ArrayList<VacationData>): GridWorkerMainFragment {
            return GridWorkerMainFragment().apply {
                arguments = Bundle().apply {
                    //                    putInt(num, Number)
                    vacationList = v
                }
            }
        }
    }

    /**
     * 캘린더뷰 세팅
     */

    fun setCalendarView(date: LocalDate, selectedDate: LocalDate) {
        calendarAdapter = CalendarAdapter(mContext, JWBaseActivity().getCalendar(date), selectedDate, R.layout.grid_item_worker_main)
        calendarAdapter.setVacationData(vacationList)
        grid_worker.adapter = calendarAdapter
    }

}