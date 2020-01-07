package com.example.uohih.joowon.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.uohih.joowon.worker.GridWorkerMainFragment
import com.example.uohih.joowon.worker.ListWorkerMainFragment


class WorkerMainAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragments = arrayListOf(
            GridWorkerMainFragment.newInstance(),
            ListWorkerMainFragment()
    )

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

}