package com.example.uohih.joowon.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.uohih.joowon.database.VacationData
import com.example.uohih.joowon.view.worker.GridWorkerMainFragment
import com.example.uohih.joowon.view.worker.ListWorkerMainFragment


class WorkerMainAdapter(fm: FragmentManager, vacationDataList: ArrayList<VacationData>) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragments = arrayListOf(
            GridWorkerMainFragment.newInstance(vacationDataList),
            ListWorkerMainFragment.newInstance(vacationDataList)
    )

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

}