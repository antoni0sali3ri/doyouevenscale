package com.github.antoni0sali3ri.doyouevenscale.prefs.enums

import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.ConstantFretSpacing
import com.github.antoni0sali3ri.doyouevenscale.core.model.EqualTemperamentFretSpacing
import com.github.antoni0sali3ri.doyouevenscale.core.model.FretSpacing

enum class FretSpacingPreference(val nameRes: Int, val mode: FretSpacing) {
    Constant(R.string.fret_spacing_constant, ConstantFretSpacing),
    EqualTemperament(R.string.fret_spacing_equal_temperament, EqualTemperamentFretSpacing);

    companion object {
        operator fun invoke(ordinal: Int) = when (ordinal) {
            in values().indices -> values()[ordinal]
            else -> throw IllegalArgumentException()
        }
    }
}