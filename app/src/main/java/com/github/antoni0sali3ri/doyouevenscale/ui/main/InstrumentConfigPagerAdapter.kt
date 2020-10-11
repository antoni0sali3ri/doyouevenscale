package com.github.antoni0sali3ri.doyouevenscale.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.InstrumentPreset
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.FretboardFragment

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class InstrumentConfigPagerAdapter(
    private val presets: List<InstrumentPreset>, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return FretboardFragment.newInstance(presets[position].id)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return presets[position].name
    }

    override fun getCount(): Int {
        return presets.size
    }
}