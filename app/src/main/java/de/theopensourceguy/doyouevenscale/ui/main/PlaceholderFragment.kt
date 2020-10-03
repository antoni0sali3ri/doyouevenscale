package de.theopensourceguy.doyouevenscale.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.model.Note
import de.theopensourceguy.doyouevenscale.core.model.Predef
import de.theopensourceguy.doyouevenscale.core.model.Scale

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val fretboardView: FretboardView = root.findViewById(R.id.fretboardView)
        fretboardView.instrument = Predef.Instruments.Mandolin
        fretboardView.scale = Scale(Note.G, Predef.ScaleTypes.Aeolian)
        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}