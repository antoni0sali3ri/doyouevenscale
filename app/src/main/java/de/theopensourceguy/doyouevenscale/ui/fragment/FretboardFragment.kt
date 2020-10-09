package de.theopensourceguy.doyouevenscale.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import de.theopensourceguy.doyouevenscale.MyApp
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.model.*
import de.theopensourceguy.doyouevenscale.ui.fragment.dialog.FretRangePickerDialog
import de.theopensourceguy.doyouevenscale.ui.fragment.dialog.NotePickerDialog
import de.theopensourceguy.doyouevenscale.ui.view.FretboardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A placeholder fragment containing a simple view.
 */
class FretboardFragment : Fragment(), AdapterView.OnItemSelectedListener,
    ObservableInstrumentConfiguration.OnChangeListener,
    FretRangePickerDialog.ResultListener,
    NotePickerDialog.ResultListener {

    private val TAG: String = "FretboardFragment"
    private val spinnerItemLayout = R.layout.layout_spinner_item

    private val tuningViewModel: TuningViewModel by activityViewModels()
    private val scaleViewModel: ScaleViewModel by activityViewModels()

    private lateinit var instrumentConfig: ObservableInstrumentConfiguration

    private lateinit var layControlsAdvanced: List<TableRow>
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach(context = $context)")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(savedInstanceState = $savedInstanceState")
        if (savedInstanceState != null) {
            instrumentConfig = savedInstanceState.getParcelable(ARG_INSTRUMENT_CONFIG)!!
        } else {
            lifecycleScope.launch(Dispatchers.IO) {

                with(MyApp.getDatabase(requireContext())) {

                    val configId = requireArguments().getLong(ARG_INSTRUMENT_CONFIG_ID)
                    val cfg = instrumentConfigDao().getSingle(configId)
                    val instrument = instrumentDao().getSingle(cfg.instrumentId)
                    val tuning = tuningDao().getSingle(cfg.tuningId)
                    Log.d(TAG, "Tuning = $tuning")
                    val scaleType = scaleDao().getSingle(cfg.scaleTypeId)

                    instrumentConfig = ObservableInstrumentConfiguration(
                        instrument,
                        tuning,
                        cfg.rootNote,
                        scaleType,
                        cfg.fromFret..cfg.toFret,
                        Note.Display.Sharp
                    )
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            Log.d(TAG, "lifecycleScope.launchWhenStarted { initializeViews() }")
            initializeViews()
        }
    }

    private fun initializeViews() {
        txtNote.text = instrumentConfig.rootNote.nameSharp

        spinnerMinFret.text = instrumentConfig.fretsShown.first.toString()
        spinnerMaxFret.text = instrumentConfig.fretsShown.last.toString()

        tuningViewModel.items.observe(viewLifecycleOwner) {
            val tunings = it.filter { it.numStrings == instrumentConfig.instrument.numStrings }
            spinnerTuning.adapter = ArrayAdapter(
                requireContext(),
                spinnerItemLayout,
                tunings.map { it.name }
            ).apply {
                notifyDataSetChanged()
            }
            val position = tunings.indexOf(instrumentConfig.tuning)
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
            val position = it.indexOf(instrumentConfig.scaleType)
            spinnerScale.setSelection(position)
        }

        fretboardView.setStringCount(instrumentConfig.instrument.numStrings)
        fretboardView.updateFretboard(instrumentConfig.fretsShown, EqualTemperamentFretSpacing)
        fretboardView.updateStringLabels(instrumentConfig.tuning, instrumentConfig.noteDisplay)
        val inst = TunedInstrument(instrumentConfig.instrument, instrumentConfig.tuning)
        val scale = Scale(instrumentConfig.rootNote, instrumentConfig.scaleType)
        fretboardView.updateScale(
            inst.getFretsForScale(scale, instrumentConfig.fretsShown),
            inst.getRoots(scale, instrumentConfig.fretsShown)
        )

        instrumentConfig.listeners.add(this@FretboardFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView(savedInstanceState = $savedInstanceState")
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

        if (instrumentConfig != null) {
            initializeViews()
        } else {
            Log.d(TAG, "instrumentConfig == null")
        }
        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putParcelable(ARG_INSTRUMENT_CONFIG, instrumentConfig)
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
        private const val ARG_INSTRUMENT_CONFIG = "instrument_config"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(instrumentConfigId: Long): FretboardFragment {
            return FretboardFragment().apply {
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
            spinnerScale -> {
                val newScaleType = scaleViewModel.items.value!!.get(position)
                instrumentConfig.scaleType = newScaleType
            }
            spinnerTuning -> {
                val newTuning = tuningViewModel.items.value!!.filter {
                    it.numStrings == instrumentConfig.instrument.numStrings
                }.get(position)
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

        fretboardView.scaleToSize()
        fretboardView.postInvalidate()
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