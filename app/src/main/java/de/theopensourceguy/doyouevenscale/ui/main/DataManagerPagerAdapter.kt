package de.theopensourceguy.doyouevenscale.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.ui.fragment.InstrumentListFragment
import de.theopensourceguy.doyouevenscale.ui.fragment.ScaleListFragment
import de.theopensourceguy.doyouevenscale.ui.fragment.TuningListFragment

val TAB_TITLES = listOf(
    R.string.tab_title_dm_instruments,
    R.string.tab_title_dm_tunings,
    R.string.tab_title_dm_scales
)
/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class DataManagerPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> InstrumentListFragment()
        1 -> TuningListFragment()
        2 -> ScaleListFragment()
        else -> throw IndexOutOfBoundsException()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}