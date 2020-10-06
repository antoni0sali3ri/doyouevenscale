package de.theopensourceguy.doyouevenscale.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import de.theopensourceguy.doyouevenscale.MyApp
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.db.ApplicationDatabase
import de.theopensourceguy.doyouevenscale.core.model.*

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment(), AdapterView.OnItemSelectedListener,
    ObservableInstrumentConfiguration.OnChangeListener,
    FretRangePickerDialog.ResultListener,
    NotePickerDialog.ResultListener {

    private val TAG: String = "FretboardFragment"
    private val spinnerItemLayout = R.layout.layout_spinner_item

    private lateinit var db: ApplicationDatabase

    private lateinit var instrumentConfig: ObservableInstrumentConfiguration

    private lateinit var scaleTypes: LiveData<List<Scale.Type>>
    private lateinit var tunings: LiveData<List<Instrument.Tuning>>

    private lateinit var layControlsAdvanced: ViewGroup
    private lateinit var layFretRange: ViewGroup
    private lateinit var btnExpandControls: ImageView
    private var controlsExpanded: Boolean = false

    private lateinit var txtNote: TextView
    private lateinit var spinnerNote: Spinner
    private lateinit var spinnerScale: Spinner
    private lateinit var spinnerTuning: Spinner
    private lateinit var spinnerMinFret: TextView
    private lateinit var spinnerMaxFret: TextView
    private lateinit var fretboardView: FretboardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = MyApp.database
        with(db) {
            val configId = arguments!!.getLong(ARG_INSTRUMENT_CONFIG_ID)
            val cfg = instrumentConfigDao().getInstrumentConfigById(configId)
            val instrument = instrumentDao().getInstrumentById(cfg.instrumentId)
            val tuning = tuningDao().getTuningById(cfg.tuningId)
            val scaleType = scaleDao().getScaleTypeById(cfg.scaleTypeId)

            instrumentConfig = ObservableInstrumentConfiguration(
                instrument,
                tuning,
                cfg.rootNote,
                scaleType,
                cfg.fromFret..cfg.toFret,
                Note.Display.Sharp
            )

            instrumentConfig.listeners.add(this@PlaceholderFragment)

            scaleTypes = scaleDao().getAllScaleTypes()
            tunings = tuningDao().getTuningsByStringCount(instrument.numStrings)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        layControlsAdvanced = root.findViewById(R.id.layFretboardControlsAdvanced)

        layFretRange = root.findViewById(R.id.layFretRange)
        layFretRange.setOnClickListener {
            showFretRangeDialog()
        }


        btnExpandControls = root.findViewById(R.id.btnExpandControls)
        btnExpandControls.setOnClickListener {
            layControlsAdvanced.visibility = if (controlsExpanded) View.GONE else View.VISIBLE
            btnExpandControls.setImageResource(
                if (controlsExpanded) R.drawable.ic_expand_less else R.drawable.ic_expand_more
            )
            controlsExpanded = !controlsExpanded
        }

        txtNote = root.findViewById(R.id.txtRootNote)
        txtNote.setOnClickListener {
            showNotePickerDialog()
        }
        txtNote.text = instrumentConfig.rootNote.nameSharp

        /*
        spinnerNote = root.findViewById(R.id.spinnerRootNote)
        spinnerNote.adapter = NoteSpinnerAdapter()
        spinnerNote.onItemSelectedListener = this
        spinnerNote.setSelection(instrumentConfig.rootNote.ordinal)
        */

        spinnerScale = root.findViewById(R.id.spinnerScaleType)
        scaleTypes.observe(viewLifecycleOwner, {
            spinnerScale.adapter = ArrayAdapter(
                context!!,
                spinnerItemLayout,
                it.map { it.name }
            )
            val position = it.indexOf(instrumentConfig.scaleType)
            spinnerScale.setSelection(position)
        })
        spinnerScale.onItemSelectedListener = this

        spinnerTuning = root.findViewById(R.id.spinnerTuning)
        tunings.observe(viewLifecycleOwner, {
            spinnerTuning.adapter = ArrayAdapter(
                context!!,
                spinnerItemLayout,
                it.map { it.tuningName }
            )
            val position = it.indexOf(instrumentConfig.tuning)
            spinnerTuning.setSelection(position)
        })
        spinnerTuning.onItemSelectedListener = this

        spinnerMinFret = root.findViewById(R.id.spinnerMinFret)
        spinnerMinFret.text = instrumentConfig.fretsShown.first.toString()
        spinnerMaxFret = root.findViewById(R.id.spinnerMaxFret)
        spinnerMaxFret.text = instrumentConfig.fretsShown.last.toString()

        fretboardView = root.findViewById(R.id.fretboardView)
        fretboardView.setStringCount(instrumentConfig.instrument.numStrings)
        fretboardView.updateFretboard(instrumentConfig.fretsShown, EqualTemperamentFretSpacing)
        fretboardView.updateStringLabels(instrumentConfig.tuning, instrumentConfig.noteDisplay)
        return root
    }

    inner class NoteSpinnerAdapter : BaseAdapter() {

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

    fun showNotePickerDialog() {
        val dialog = NotePickerDialog(this, instrumentConfig.noteDisplay)
        dialog.show(childFragmentManager, "NotePickerDialog")
    }

    fun showFretRangeDialog() {
        val dialog = FretRangePickerDialog(instrumentConfig.fretsShown, this)
        dialog.show(childFragmentManager, "RangePickerDialog")
    }

    companion object {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_INSTRUMENT_CONFIG_ID = "instrument_config_id"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(instrumentConfigId: Long): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_INSTRUMENT_CONFIG_ID, instrumentConfigId)
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent) {
            null -> {
            }
            /*
            spinnerNote -> {
                val newRoot = Note.values()[position]
                instrumentConfig.rootNote = newRoot
            }
            */
            spinnerScale -> {
                val newScaleType = scaleTypes.value!!.get(position)
                instrumentConfig.scaleType = newScaleType
            }
            spinnerTuning -> {
                val newTuning = tunings.value!!.get(position)
                instrumentConfig.tuning = newTuning
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onTuningChanged(newTuning: Instrument.Tuning, oldTuning: Instrument.Tuning) {
        fretboardView.updateStringLabels(newTuning, instrumentConfig.noteDisplay)

        val inst = TunedInstrument(instrumentConfig.instrument, newTuning)
        val scale = Scale(instrumentConfig.rootNote, instrumentConfig.scaleType)
        fretboardView.updateScale(
            inst.getFretsForScale(scale, instrumentConfig.fretsShown),
            inst.getRoots(scale, instrumentConfig.fretsShown)
        )

        fretboardView.scaleToSize()
        fretboardView.postInvalidate()
    }

    override fun onScaleChanged(newScale: Scale, oldScale: Scale) {
        val inst = TunedInstrument(instrumentConfig.instrument, instrumentConfig.tuning)
        fretboardView.updateScale(
            inst.getFretsForScale(newScale, instrumentConfig.fretsShown),
            inst.getRoots(newScale, instrumentConfig.fretsShown)
        )
        txtNote.text = newScale.root.getName(instrumentConfig.noteDisplay)
        fretboardView.scaleToSize()
        fretboardView.postInvalidate()
    }

    override fun onFretRangeChanged(newRange: IntRange, oldRange: IntRange) {
        fretboardView.updateFretboard(newRange, EqualTemperamentFretSpacing)
        val inst = TunedInstrument(instrumentConfig.instrument, instrumentConfig.tuning)
        val scale = Scale(instrumentConfig.rootNote, instrumentConfig.scaleType)
        fretboardView.updateScale(
            inst.getFretsForScale(scale, newRange),
            inst.getRoots(scale, newRange)
        )
        fretboardView.scaleToSize()
        fretboardView.postInvalidate()
    }

    override fun onNoteDisplayChanged(newDisplay: Note.Display, oldDisplay: Note.Display) {
        txtNote.text = instrumentConfig.rootNote.getName(newDisplay)
        fretboardView.updateStringLabels(instrumentConfig.tuning, newDisplay)
    }

    override fun updateFretRange(range: IntRange) {
        spinnerMinFret.text = range.first.toString()
        spinnerMaxFret.text = range.last.toString()
        instrumentConfig.fretsShown = range
    }

    override fun onNoteSelected(note: String, displayMode: Note.Display) {
        instrumentConfig.rootNote = Note.valueOf(note)
        instrumentConfig.noteDisplay = displayMode
    }
}