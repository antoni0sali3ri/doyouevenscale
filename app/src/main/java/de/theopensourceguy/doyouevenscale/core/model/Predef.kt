package de.theopensourceguy.doyouevenscale.core.model

import android.content.Context
import androidx.annotation.StringRes
import de.theopensourceguy.doyouevenscale.R

//TODO: Move these to resources
class Predef(private val context: Context) {

    fun getString(@StringRes res: Int): String = context.resources.getString(res)

    val configs = listOf(
        InstrumentConfiguration(1, 1, 1, Note.C, 0, 12),
        InstrumentConfiguration(2, 2, 1, Note.C, 0, 12),
        InstrumentConfiguration(3, 3, 1, Note.C, 0, 12),
        InstrumentConfiguration(4, 4, 1, Note.C, 0, 12),
        InstrumentConfiguration(5, 10, 1, Note.C, 0, 12)
    )

    val instruments = listOf(
        Instrument(6, getString(R.string.instrument_guitar_name)).apply { id = 1 },
        Instrument(4, getString(R.string.instrument_bass_name)).apply { id = 2 },
        Instrument(4, getString(R.string.instrument_mandolin_name)).apply { id = 3 },
        Instrument(5, getString(R.string.instrument_banjo_name)).apply { id = 4 },
        Instrument(4, getString(R.string.instrument_ukulele_name)).apply { id = 5 }
    )

    val tunings = listOf(
        Instrument.Tuning(
            listOf(Note.E, Note.A, Note.D, Note.G, Note.B, Note.E),
            getString(R.string.tuning_guitar_standard_name)
        ).apply { id = 1 },
        Instrument.Tuning(
            listOf(Note.E, Note.A, Note.D, Note.G),
            getString(R.string.tuning_bass_name)
        ).apply { id = 2 },
        Instrument.Tuning(
            listOf(Note.G, Note.D, Note.A, Note.E),
            getString(R.string.tuning_violin_name)
        ).apply { id = 3 },
        Instrument.Tuning(
            listOf(Note.G, Note.D, Note.G, Note.B, Note.D),
            getString(R.string.tuning_banjo_openg_name)
        ).apply { id = 4 },
        Instrument.Tuning(
            listOf(Note.B, Note.E, Note.A, Note.D, Note.G),
            getString(R.string.tuning_bass_5str_name)
        ).apply { id = 5 },
        Instrument.Tuning(
            listOf(Note.B, Note.E, Note.A, Note.D, Note.G, Note.C),
            getString(R.string.tuning_bass_6str_name)
        ).apply { id = 6 },
        Instrument.Tuning(
            listOf(Note.C, Note.G, Note.D, Note.A),
            getString(R.string.tuning_viola_name)
        ).apply { id = 7 },
        Instrument.Tuning(
            listOf(Note.Dis, Note.Gis, Note.Cis, Note.Fis, Note.Ais, Note.Dis),
            getString(R.string.tuning_guitar_halfdown_name)
        ).apply { id = 8 },
        Instrument.Tuning(
            listOf(Note.D, Note.G, Note.C, Note.F, Note.A, Note.D),
            getString(R.string.tuning_guitar_fulldown_name)
        ).apply { id = 9 },
        Instrument.Tuning(
            listOf(Note.G, Note.C, Note.E, Note.A),
            getString(R.string.tuning_ukulele_standard_name)
        ).apply { id = 10 }
    )

    val scaleTypes = listOf(
        Scale.Type(
            getString(R.string.scale_ionian_name),
            2, 4, 5, 7, 9, 11
        ).apply { id = 1 },
        Scale.Type(
            getString(R.string.scale_dorian_name),
            2, 3, 5, 7, 9, 10
        ).apply { id = 2 },
        Scale.Type(
            getString(R.string.scale_phrygian_name),
            1, 3, 5, 7, 8, 10
        ).apply { id = 3 },
        Scale.Type(
            getString(R.string.scale_lydian_name),
            2, 4, 6, 7, 9, 11
        ).apply { id = 4 },
        Scale.Type(
            getString(R.string.scale_mixolydian_name),
            2, 4, 5, 7, 9, 10
        ).apply { id = 5 },
        Scale.Type(
            getString(R.string.scale_aeolian_name),
            2, 3, 5, 7, 8, 10
        ).apply { id = 6 },
        Scale.Type(
            getString(R.string.scale_locrian_name),
            1, 3, 5, 6, 8, 10
        ).apply { id = 7 },
        Scale.Type(
            getString(R.string.scale_harmonic_name),
            2, 3, 5, 7, 8, 11
        ).apply { id = 8 },
        Scale.Type(
            getString(R.string.scale_melodic_name),
            2, 3, 5, 7, 9, 11
        ).apply { id = 9 },
        Scale.Type(
            getString(R.string.scale_pentatonic_major_name),
            2, 4, 7, 9
        ).apply { id = 10 },
        Scale.Type(
            getString(R.string.scale_pentatonic_minor_name),
            3, 5, 7, 10
        ).apply { id = 11 },
        Scale.Type(
            getString(R.string.scale_pentatonic_blues_name),
            3, 5, 6, 7, 10
        ).apply { id = 12 }
    )
}

