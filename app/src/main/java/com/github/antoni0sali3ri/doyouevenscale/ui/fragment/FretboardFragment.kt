package com.github.antoni0sali3ri.doyouevenscale.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.db.ApplicationDatabase
import com.github.antoni0sali3ri.doyouevenscale.core.model.*
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Scale
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.dialog.FretRangePickerDialog
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.dialog.NotePickerDialog
import com.github.antoni0sali3ri.doyouevenscale.ui.view.FretboardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A placeholder fragment containing a simple view.
 */
class FretboardFragment : Fragment(), AdapterView.OnItemSelectedListener,
    ObservableInstrumentPreset.OnChangeListener,
    FretRangePickerDialog.ResultListener,
    NotePickerDialog.ResultListener {

    private val spinnerItemLayout = R.layout.layout_spinner_item

    private val tuningViewModel: TuningViewModel by activityViewModels()
    private val scaleViewModel: ScaleViewModel by activityViewModels()

    private lateinit var instrumentPreset: ObservableInstrumentPreset

    private lateinit var layControlsAdvanced: List<TableRow>
    private lateinit var layFretRange: ViewGroup
    private lateinit var btnExpandControls: ImageView
    private var controlsExpanded: Boolean = false

    private lateinit var txtNote: TextView
    private lateinit var spinnerScale: Spinner
    private lateinit var spinnerTuning: Spinner
    private lateinit var spinnerMinFret: TextView
    private lateinit var spinnerMaxFret: TextView
    private lateinit var fretboardView: FretboardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            instrumentPreset = savedInstanceState.getParcelable(ARG_INSTRUMENT_PRESET)!!
        } else {
            lifecycleScope.launch(Dispatchers.IO) {

                with(ApplicationDatabase.getDatabase(requireContext())) {

                    val presetId = requireArguments().getLong(ARG_INSTRUMENT_PRESET_ID)
                    val preset = instrumentPresetDao().getSingle(presetId)
                    val instrument = instrumentDao().getSingle(preset.instrumentId)
                    val tuning = tuningDao().getSingle(preset.tuningId)
                    val scaleType = scaleDao().getSingle(preset.scaleTypeId)

                    instrumentPreset = ObservableInstrumentPreset(
                        instrument,
                        tuning,
                        preset.rootNote,
                        scaleType,
                        preset.fromFret..preset.toFret,
                        Note.Display.Sharp
                    )
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            initializeViews()
        }
    }

    private fun initializeViews() {
        txtNote.text = instrumentPreset.rootNote.nameSharp

        spinnerMinFret.text = instrumentPreset.fretsShown.first.toString()
        spinnerMaxFret.text = instrumentPreset.fretsShown.last.toString()

        tuningViewModel.items.observe(viewLifecycleOwner) {
            val tunings = it.filter { it.instrumentId == instrumentPreset.instrument.id }
            spinnerTuning.apply {
                adapter = ArrayAdapter(
                    requireContext(),
                    spinnerItemLayout,
                    tunings.map { it.name }
                ).apply {
                    notifyDataSetChanged()
                }
            }
            val position = tunings.indexOf(instrumentPreset.tuning)
            spinnerTuning.setSelection(position)
        }

        scaleViewModel.items.observe(viewLifecycleOwner) {
            spinnerScale.adapter = ArrayAdapter(
                requireContext(),
                spinnerItemLayout,
                it.map { it.name }
            ).apply {
                notifyDataSetChanged()
            }
            val position = it.indexOf(instrumentPreset.scaleType)
            spinnerScale.setSelection(position)
        }

        fretboardView.setStringCount(instrumentPreset.instrument.stringCount)
        fretboardView.updateFretboard(instrumentPreset.fretsShown, EqualTemperamentFretSpacing)
        fretboardView.updateStringLabels(instrumentPreset.tuning, instrumentPreset.noteDisplay)
        val inst = TunedInstrument(instrumentPreset.instrument, instrumentPreset.tuning)
        val scale = Scale(instrumentPreset.rootNote, instrumentPreset.scaleType)
        fretboardView.updateScale(
            inst.getFretsForScale(scale, instrumentPreset.fretsShown),
            inst.getRoots(scale, instrumentPreset.fretsShown)
        )
        updateFretboardView()

        instrumentPreset.listeners.add(this@FretboardFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_fretboard, container, false)

        layControlsAdvanced = listOf(
            root.findViewById(R.id.tblRowTuning),
            root.findViewById(R.id.tblRowFretRange)
        )

        layFretRange = root.findViewById(R.id.layFretRange)
        layFretRange.setOnClickListener {
            showFretRangeDialog()
        }


        btnExpandControls = root.findViewById(R.id.btnExpandControls)
        btnExpandControls.setOnClickListener {
            layControlsAdvanced.forEach {
                it.visibility = if (controlsExpanded) View.GONE else View.VISIBLE
            }
            btnExpandControls.setImageResource(
                if (controlsExpanded) R.drawable.ic_expand_less else R.drawable.ic_expand_more
            )
            controlsExpanded = !controlsExpanded
        }

        txtNote = root.findViewById(R.id.txtRootNote)
        txtNote.setOnClickListener {
            showNotePickerDialog()
        }

        spinnerScale = root.findViewById(R.id.spinnerScaleType)
        spinnerScale.onItemSelectedListener = this

        spinnerTuning = root.findViewById(R.id.spinnerTuning)
        spinnerTuning.onItemSelectedListener = this

        spinnerMinFret = root.findViewById(R.id.spinnerMinFret)
        spinnerMaxFret = root.findViewById(R.id.spinnerMaxFret)

        fretboardView = root.findViewById(R.id.fretboardView)

        initializeViews()
        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putParcelable(ARG_INSTRUMENT_PRESET, instrumentPreset)
        }

    }

    private fun showNotePickerDialog() {
        val dialog = NotePickerDialog(this, instrumentPreset.noteDisplay)
        dialog.show(childFragmentManager, "NotePickerDialog")
    }

    private fun showFretRangeDialog() {
        val dialog = FretRangePickerDialog(instrumentPreset.fretsShown, this)
        dialog.show(childFragmentManager, "RangePickerDialog")
    }

    private fun updateFretboardView() {
        fretboardView.scaleToSize()
        fretboardView.postInvalidate()
    }

    companion object {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_INSTRUMENT_PRESET_ID = "instrument_preset_id"
        private const val ARG_INSTRUMENT_PRESET = "instrument_preset"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(instrumentPresetId: Long): FretboardFragment {
            return FretboardFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_INSTRUMENT_PRESET_ID, instrumentPresetId)
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent) {
            null -> {
            }
            spinnerScale -> {
                scaleViewModel.items.observe(viewLifecycleOwner) {
                    val newScaleType = it.get(position)
                    instrumentPreset.scaleType = newScaleType
                }
            }
            spinnerTuning -> {
                tuningViewModel.forInstrument(instrumentPreset.instrument.id)
                    .observe(viewLifecycleOwner) {
                        instrumentPreset.tuning = it.get(position)
                    }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onTuningChanged(newTuning: Instrument.Tuning, oldTuning: Instrument.Tuning) {
        fretboardView.updateStringLabels(newTuning, instrumentPreset.noteDisplay)

        val inst = TunedInstrument(instrumentPreset.instrument, newTuning)
        val scale = Scale(instrumentPreset.rootNote, instrumentPreset.scaleType)
        fretboardView.updateScale(
            inst.getFretsForScale(scale, instrumentPreset.fretsShown),
            inst.getRoots(scale, instrumentPreset.fretsShown)
        )

        fretboardView.scaleToSize()
        fretboardView.postInvalidate()
    }

    override fun onScaleChanged(newScale: Scale, oldScale: Scale) {
        val inst = TunedInstrument(instrumentPreset.instrument, instrumentPreset.tuning)
        fretboardView.updateScale(
            inst.getFretsForScale(newScale, instrumentPreset.fretsShown),
            inst.getRoots(newScale, instrumentPreset.fretsShown)
        )
        txtNote.text = newScale.root.getName(instrumentPreset.noteDisplay)

        fretboardView.scaleToSize()
        fretboardView.postInvalidate()
    }

    override fun onFretRangeChanged(newRange: IntRange, oldRange: IntRange) {
        fretboardView.updateFretboard(newRange, EqualTemperamentFretSpacing)
        val inst = TunedInstrument(instrumentPreset.instrument, instrumentPreset.tuning)
        val scale = Scale(instrumentPreset.rootNote, instrumentPreset.scaleType)
        fretboardView.updateScale(
            inst.getFretsForScale(scale, newRange),
            inst.getRoots(scale, newRange)
        )

        fretboardView.scaleToSize()
        fretboardView.postInvalidate()
    }

    override fun onNoteDisplayChanged(newDisplay: Note.Display, oldDisplay: Note.Display) {
        txtNote.text = instrumentPreset.rootNote.getName(newDisplay)
        fretboardView.updateStringLabels(instrumentPreset.tuning, newDisplay)

        fretboardView.scaleToSize()
        fretboardView.postInvalidate()
    }

    override fun updateFretRange(range: IntRange) {
        spinnerMinFret.text = range.first.toString()
        spinnerMaxFret.text = range.last.toString()
        instrumentPreset.fretsShown = range
    }

    override fun onNoteSelected(note: Note, displayMode: Note.Display) {
        instrumentPreset.rootNote = note
        instrumentPreset.noteDisplay = displayMode
    }
}