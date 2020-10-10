package com.github.antoni0sali3ri.doyouevenscale.ui.fragment.editor

import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.fragment.app.activityViewModels
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.Instrument
import com.github.antoni0sali3ri.doyouevenscale.core.model.InstrumentViewModel

class InstrumentEditorFragment : EntityEditorFragment<Instrument>(Instrument::class.java) {
    override val layoutResource: Int = R.layout.fragment_instrument_editor

    override val templateItem: Instrument = Instrument(4, "Instrument")

    override val viewModel: InstrumentViewModel by activityViewModels()

    private lateinit var npStringCount: NumberPicker

    override fun initializeViews(item: Instrument) {
        super.initializeViews(item)

        if (item.id == 0L) {
            npStringCount.minValue = 1
            npStringCount.maxValue = Instrument.MaxStrings
        } else {
            npStringCount.minValue = item.numStrings
            npStringCount.maxValue = item.numStrings
        }
        npStringCount.value = item.numStrings
        npStringCount.setOnValueChangedListener { _, _, newVal ->
            item.numStrings = newVal
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        npStringCount = view.findViewById(R.id.numberPickerStringCount)
    }


}