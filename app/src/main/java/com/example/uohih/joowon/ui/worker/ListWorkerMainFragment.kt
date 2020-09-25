package com.example.uohih.joowon.ui.worker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uohih.joowon.R
import com.example.uohih.joowon.ui.adapter.WorkerVacationListAdapter
import com.example.uohih.joowon.database.VacationData
import kotlinx.android.synthetic.main.fragment_list_worker_main.*

class ListWorkerMainFragment : Fragment() {
    private lateinit var mContext: Context
    private var vacationList = arrayListOf<VacationData>()
    private val workerVacationListAdapter by lazy { WorkerVacationListAdapter(vacationList) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this.activity!!
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_list_worker_main, container, false)
        return root
    }

    // 뷰 생성이 완료되면 호출되는 메소드
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        section_label.text=arguments?.let{
//            it.getInt(num).toString()
//        }

        recycler_worker.setHasFixedSize(true)
        recycler_worker.layoutManager = LinearLayoutManager(mContext)

        recycler_worker.adapter = workerVacationListAdapter


    }

    companion object {
        @JvmStatic
        fun newInstance(v: ArrayList<VacationData>): ListWorkerMainFragment {
            return ListWorkerMainFragment().apply {
                arguments = Bundle().apply {
                    //                    putInt(num, Number)
                    vacationList = v
                }
            }
        }
    }

    fun setNotify() {
        workerVacationListAdapter.notifyDataSetChanged()
    }
}