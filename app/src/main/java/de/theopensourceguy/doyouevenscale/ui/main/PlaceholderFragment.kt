package de.theopensourceguy.doyouevenscale.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.ScaleViewerApplication
import de.theopensourceguy.doyouevenscale.core.db.ApplicationDatabase
import de.theopensourceguy.doyouevenscale.core.model.Instrument
import de.theopensourceguy.doyouevenscale.core.model.Note
import de.theopensourceguy.doyouevenscale.core.model.Scale
import de.theopensourceguy.doyouevenscale.core.model.TunedInstrument

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val TAG: String = "FretboardFragment"
    private val spinnerItemLayout = android.R.layout.simple_spinner_item

    private lateinit var db: ApplicationDatabase

    private lateinit var instrument: Instrument
    private lateinit var tuning: Instrument.Tuning
    private lateinit var scale: Scale

    private lateinit var scaleTypes: LiveData<List<Scale.Type>>
    private lateinit var tunings: LiveData<List<Instrument.Tuning>>

    private lateinit var spinnerNote: Spinner
    private lateinit var spinnerScale: Spinner
    private lateinit var spinnerTuning: Spinner
    private lateinit var fretboardView: FretboardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = ScaleViewerApplication.database
        with(db) {
            instrument = instrumentDao().getInstrumentById(arguments!!.getInt(ARG_INSTRUMENT_ID))
            tuning = tuningDao().getTuningForId(instrument.defaultTuningId)
            scaleTypes = scaleDao().getAllScaleTypes()
            tunings = tuningDao().getTuningsForStringCount(instrument.numStrings)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        spinnerNote = root.findViewById(R.id.spinnerRootNote)
        spinnerNote.adapter = NoteSpinnerAdapter()
        spinnerNote.onItemSelectedListener = this

        spinnerScale = root.findViewById(R.id.spinnerScaleType)
        scaleTypes.observe(viewLifecycleOwner, {
            spinnerScale.adapter = ArrayAdapter(
                context!!,
                spinnerItemLayout,
                it.map { it.name }
            )
        })
        spinnerScale.onItemSelectedListener = this

        spinnerTuning = root.findViewById(R.id.spinnerTuning)
        tunings.observe(viewLifecycleOwner, {
            spinnerTuning.adapter = ArrayAdapter(
                context!!,
                spinnerItemLayout,
                it.map { it.tuningName }
            )
        })
        spinnerTuning.onItemSelectedListener = this

        fretboardView = root.findViewById(R.id.fretboardView)
        fretboardView.instrument = TunedInstrument(instrument, tuning)
        return root
    }

    inner class NoteSpinnerAdapter : BaseAdapter()
    {

        var sharp: Boolean = false

        override fun getCount(): Int = 12

        override fun getItem(position: Int): String = Note.values()[position].let {
            if (sharp) it.nameSharp else it.nameFlat
        }

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: layoutInflater.inflate(spinnerItemLayout, parent, false)
            if (view is TextView) {
                view.text = getItem(position)
            }
            return view
        }
    }

    companion object {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_INSTRUMENT_ID = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(instrumentId: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_INSTRUMENT_ID, instrumentId)
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d(TAG, "onItemSelected(parent = $parent, position = $position)")
        when (parent) {
            null -> {}
            spinnerNote -> {
                val newRoot = Note.values()[position]
                val scaleType = fretboardView.scale?.type ?: scaleTypes.value!!.first()
                fretboardView.scale = Scale(newRoot, scaleType)
            }
            spinnerScale -> {
                val newScaleType = scaleTypes.value!!.get(position)
                val root = fretboardView.scale?.root ?: Note.C
                fretboardView.scale = Scale(root, newScaleType)
            }
            spinnerTuning -> {
                val newTuning = tunings.value!!.get(position)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}