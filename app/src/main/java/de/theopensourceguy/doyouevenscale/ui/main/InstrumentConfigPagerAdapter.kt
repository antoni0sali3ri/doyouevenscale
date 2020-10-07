package de.theopensourceguy.doyouevenscale.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import de.theopensourceguy.doyouevenscale.core.model.Instrument
import de.theopensourceguy.doyouevenscale.ui.fragment.FretboardFragment

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class InstrumentConfigPagerAdapter(private val instruments: List<Instrument>, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return FretboardFragment.newInstance(instruments[position].id)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return instruments[position].name
    }

    override fun getCount(): Int {
        return instruments.size
    }
}