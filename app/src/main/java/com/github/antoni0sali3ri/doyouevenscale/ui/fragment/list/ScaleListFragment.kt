package com.github.antoni0sali3ri.doyouevenscale.ui.fragment.list

import androidx.fragment.app.activityViewModels
import com.github.antoni0sali3ri.doyouevenscale.core.model.ScaleViewModel
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Scale

class ScaleListFragment : EntityListFragment<Scale.Type>(Scale.Type::class.java) {
    override val viewModel: ScaleViewModel by activityViewModels()
}