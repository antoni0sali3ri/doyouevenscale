package com.github.antoni0sali3ri.doyouevenscale.ui.main

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.*

val TAB_TITLES = listOf(
    R.string.tab_title_dm_presets,
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

    private val items: MutableMap<Int, EntityListFragment<*>> = mutableMapOf()

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> InstrumentPresetListFragment()
        1 -> InstrumentListFragment()
        2 -> TuningListFragment()
        3 -> ScaleListFragment()
        else -> throw IndexOutOfBoundsException()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 4
    }

    override fun instantiateItem(container: ViewGroup, position: Int): EntityListFragment<*> {
        val fragment = super.instantiateItem(container, position) as EntityListFragment<*>
        items.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        items.remove(position)
    }

    fun getFragmentAt(position: Int) = items[position]
}