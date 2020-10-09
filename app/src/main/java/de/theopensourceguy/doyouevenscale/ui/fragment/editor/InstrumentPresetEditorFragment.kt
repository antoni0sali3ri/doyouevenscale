package de.theopensourceguy.doyouevenscale.ui.fragment.editor

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.model.InstrumentPreset
import de.theopensourceguy.doyouevenscale.core.model.Note
import de.theopensourceguy.doyouevenscale.core.model.PresetViewModel

class InstrumentPresetEditorFragment :
    EntityEditorFragment<InstrumentPreset>(InstrumentPreset::class.java) {
    override val layoutResource: Int = R.layout.fragment_instrument_preset_editor

    override val templateItem: InstrumentPreset = InstrumentPreset(0, 0, 0, Note.C, 0, 12)

    override val viewModel: PresetViewModel by activityViewModels()

    override fun initializeViews(item: InstrumentPreset) {
        super.initializeViews(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSubmit.isEnabled = false
    }
}