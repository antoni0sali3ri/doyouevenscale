package de.theopensourceguy.doyouevenscale.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.model.Instrument

private val TAB_TITLES = arrayOf(
        R.string.instrument_guitar_name,
        R.string.instrument_mandolin_name,
        R.string.instrument_banjo_name,
        R.string.instrument_bass_name
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(val instruments: List<Instrument>, private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return PlaceholderFragment.newInstance(instruments[position].instrumentId)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return instruments[position].name
    }

    override fun getCount(): Int {
        return instruments.size
    }
}