package com.github.antoni0sali3ri.doyouevenscale.ui.fragment.editor

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.*
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.InstrumentPreset
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Scale
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.dialog.FretRangePickerDialog
import com.github.antoni0sali3ri.doyouevenscale.ui.fragment.dialog.NotePickerDialog

class InstrumentPresetEditorFragment :
    EntityEditorFragment<InstrumentPreset>(InstrumentPreset::class.java),
    NotePickerDialog.ResultListener,
    FretRangePickerDialog.ResultListener,
    AdapterView.OnItemSelectedListener {

    private val spinnerItemLayout = R.layout.layout_spinner_item
    override val layoutResource: Int = R.layout.fragment_instrument_preset_editor

    override val templateItem: InstrumentPreset = InstrumentPreset(0, 0, 0, Note.C, 0, 12)

    override val viewModel: PresetViewModel by activityViewModels()
    private val instrumentViewModel: InstrumentViewModel by activityViewModels()
    private val tuningViewModel: TuningViewModel by activityViewModels()
    private val scaleViewModel: ScaleViewModel by activityViewModels()

    private lateinit var spinnerTunings: Spinner
    private lateinit var spinnerInstrument: Spinner
    private lateinit var spinnerScale: Spinner
    private lateinit var spinnerRootNote: TextView
    private lateinit var txtFretMin: TextView
    private lateinit var txtFretMax: TextView

    private val instruments: MutableList<Instrument> = mutableListOf()
    private val tunings: MutableList<Instrument.Tuning> = mutableListOf()
    private val scaleTypes: MutableList<Scale.Type> = mutableListOf()

    override fun validateItem(): Boolean {
        return super.validateItem()
    }

    override fun initializeViews(item: InstrumentPreset) {
        super.initializeViews(item)

        txtFretMin.text = item.fromFret.toString()
        txtFretMax.text = item.toFret.toString()

        spinnerRootNote.setOnClickListener {
            NotePickerDialog(this, Note.Display.Sharp).show(
                childFragmentManager,
                "NotePickerDialog"
            )
        }
        spinnerRootNote.text = item.rootNote.getName(Note.Display.Sharp)

        spinnerInstrument.onItemSelectedListener = this
        spinnerScale.onItemSelectedListener = this
        spinnerTunings.onItemSelectedListener = this

        instrumentViewModel.items.observe(viewLifecycleOwner) { instruments ->
            this.instruments.clear()
            this.instruments.addAll(instruments)
            spinnerInstrument.adapter = ArrayAdapter(
                requireContext(),
                spinnerItemLayout,
                instruments.map { it.name }
            )
            // TODO: This will blow up if there are no instruments defined
            val index =
                if (item.instrumentId == 0L) 0 else instruments.indexOfFirst { it.id == item.instrumentId }
            spinnerInstrument.setSelection(index)
            item.instrumentId = instruments[index].id

            populateTuningSpinner(instruments[index])
        }

        scaleViewModel.items.observe(viewLifecycleOwner) { scaleTypes ->
            this.scaleTypes.clear()
            this.scaleTypes.addAll(scaleTypes)
            spinnerScale.adapter = ArrayAdapter(
                requireContext(),
                spinnerItemLayout,
                scaleTypes.map { it.name }
            )
            // TODO: This will blow up if there are no tunings defined
            val index =
                if (item.scaleTypeId == 0L) 0 else scaleTypes.indexOfFirst { it.id == item.scaleTypeId }
            spinnerScale.setSelection(index)
            item.scaleTypeId = scaleTypes[index].id
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layFretRange: ViewGroup = view.findViewById(R.id.layFretRange)
        layFretRange.setOnClickListener {
            FretRangePickerDialog(item.fromFret..item.toFret, this).show(
                childFragmentManager,
                "FretRangePickerDialog"
            )
        }
        spinnerInstrument = view.findViewById(R.id.spinnerPresetInstrument)
        spinnerTunings = view.findViewById(R.id.spinnerPresetTuning)
        spinnerScale = view.findViewById(R.id.spinnerPresetScaleType)
        spinnerRootNote = view.findViewById(R.id.spinnerPresetRootNote)

        txtFretMin = view.findViewById(R.id.txtFretRangeMin)
        txtFretMax = view.findViewById(R.id.txtFretRangeMax)
    }

    override fun onNoteSelected(note: Note, displayMode: Note.Display) {
        item.rootNote = note
        spinnerRootNote.text = note.getName(displayMode)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent) {
            spinnerInstrument -> {
                item.instrumentId = instruments[position].id
                populateTuningSpinner(instruments[position])
            }
            spinnerScale -> {
                item.scaleTypeId = scaleTypes[position].id
            }
            spinnerTunings -> {
                item.tuningId = tunings[position].id
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun populateTuningSpinner(instrument: Instrument) {
        Log.d(TAG, "populateTuningSpinner(instrument = $instrument)")
        val tuningsLiveData = tuningViewModel.forInstrument(instrument.id)
        tuningsLiveData.observe(viewLifecycleOwner) { tunings ->
            Log.d(TAG, "LiveData.Observer(tuningId = ${item.tuningId}, tunings = $tunings)")
            this.tunings.clear()
            this.tunings.addAll(tunings)
            spinnerTunings.adapter = ArrayAdapter(
                requireContext(),
                R.layout.layout_spinner_item,
                tunings.map { it.name }
            ).apply {
                notifyDataSetChanged()
            }
            spinnerTunings.invalidate()
            // TODO: This is gonna blow up if there are no tunings defined for this instrument yet
            var index = 0
            val tuningIndex = tunings.indexOfFirst { it.id == item.tuningId }
            if (tuningIndex >= 0) {
                index = tuningIndex
            }
            spinnerTunings.setSelection(index)
            item.tuningId = tunings[index].id
        }

    }

    val TAG = this::class.java.simpleName

    override fun updateFretRange(range: IntRange) {
        txtFretMin.text = range.first.toString()
        txtFretMax.text = range.last.toString()

        item.fromFret = range.first
        item.toFret = range.last
    }
}