package com.example.uohih.joowon.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.uohih.joowon.R

class BlankFragment : Fragment() {
    private lateinit var mContext: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this.activity!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_blank, container, false)
        return root
    }

    // 뷰 생성이 완료되면 호출되는 메소드

    companion object {
        @JvmStatic
        fun newInstance(): BlankFragment {
            return BlankFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }

        fun newInstance(bundle: Bundle): BlankFragment {
            return BlankFragment().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }


}