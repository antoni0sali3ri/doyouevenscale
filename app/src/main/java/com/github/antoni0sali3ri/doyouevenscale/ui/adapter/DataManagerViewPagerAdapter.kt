package com.github.antoni0sali3ri.doyouevenscale.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.list.InstrumentListFragment
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.list.InstrumentPresetListFragment
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.list.ScaleListFragment
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.list.TuningListFragment

class DataManagerViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> InstrumentPresetListFragment()
        1 -> InstrumentListFragment()
        2 -> TuningListFragment()
        3 -> ScaleListFragment()
        else -> throw IndexOutOfBoundsException()
    }
}