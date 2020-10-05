package de.theopensourceguy.doyouevenscale.ui.main

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.ScaleViewerApplication

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
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    val prefs = ScaleViewerApplication.prefs

    override fun getItem(position: Int): Fragment {
        val instrumentId = prefs.core.getInstrumentIdList()[position]
        return PlaceholderFragment.newInstance(instrumentId)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "TEST"
    }

    override fun getCount(): Int {
        val count = prefs.core.getInstrumentIdList().size
        Log.d("Adapter", "$count")
        return count
    }
}