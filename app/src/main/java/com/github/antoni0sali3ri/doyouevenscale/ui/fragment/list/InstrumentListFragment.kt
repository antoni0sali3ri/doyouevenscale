package com.github.antoni0sali3ri.doyouevenscale.ui.fragment.list

import androidx.fragment.app.activityViewModels
import com.github.antoni0sali3ri.doyouevenscale.core.model.InstrumentViewModel
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument

class InstrumentListFragment : EntityListFragment<Instrument>(Instrument::class.java) {
    override val viewModel: InstrumentViewModel by activityViewModels()
}