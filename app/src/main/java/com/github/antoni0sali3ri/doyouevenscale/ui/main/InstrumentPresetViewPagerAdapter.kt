package com.github.antoni0sali3ri.doyouevenscale.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.InstrumentPreset
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.FretboardFragment

class InstrumentPresetViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val presets: List<InstrumentPreset>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = presets.size

    override fun createFragment(position: Int): Fragment =
        FretboardFragment.newInstance(presets[position].id)
}