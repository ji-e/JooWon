package com.example.uohih.joowon.worker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.uohih.joowon.R

class ListWorkerMainFragment : Fragment() {
    private lateinit var mContext: Context

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
    }
}