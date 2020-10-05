package de.theopensourceguy.doyouevenscale.core.model

import android.content.Context
import androidx.annotation.StringRes
import de.theopensourceguy.doyouevenscale.R

//TODO: Move these to resources
class Predef(private val context: Context) {

    fun getString(@StringRes res: Int): String = context.resources.getString(res)

    val instruments = listOf(
        TunedInstrument(
            Instrument(6, 12, getString(R.string.instrument_guitar_name), -1),
            Instrument.Tuning(
                listOf(Note.E, Note.A, Note.D, Note.G, Note.B, Note.E),
                getString(R.string.tuning_guitar_standard_name)
            )
        ),
        TunedInstrument(
            Instrument(4, 12, getString(R.string.instrument_bass_name), -1),
            Instrument.Tuning(
                listOf(Note.E, Note.A, Note.D, Note.G),
                getString(R.string.tuning_bass_name)
            )
        ),
        TunedInstrument(
            Instrument(4, 12, getString(R.string.instrument_mandolin_name), -1),
            Instrument.Tuning(
                listOf(Note.G, Note.D, Note.A, Note.E),
                getString(R.string.tuning_violin_name)
            )
        ),
        TunedInstrument(
            Instrument(5, 12, getString(R.string.instrument_banjo_name), -1),
            Instrument.Tuning(
                listOf(Note.G, Note.D, Note.G, Note.B, Note.D),
                getString(R.string.tuning_banjo_openg_name)
            )
        )
    )


    val tunings = listOf(
        Instrument.Tuning(
            listOf(Note.B, Note.E, Note.A, Note.D, Note.G),
            getString(R.string.tuning_bass_5str_name)
        ),
        Instrument.Tuning(
            listOf(Note.B, Note.E, Note.A, Note.D, Note.G, Note.C),
            getString(R.string.tuning_bass_6str_name)
        ),
        Instrument.Tuning(
            listOf(Note.C, Note.G, Note.D, Note.A),
            getString(R.string.tuning_viola_name)
        )
    )

    val scaleTypes = listOf(
        Scale.Type(
            getString(R.string.scale_ionian_name),
            2, 4, 5, 7, 9, 11
        ),
        Scale.Type(
            getString(R.string.scale_dorian_name),
            2, 3, 5, 7, 9, 10
        ),
        Scale.Type(
            getString(R.string.scale_phrygian_name),
            1, 3, 5, 7, 8, 10
        ),
        Scale.Type(
            getString(R.string.scale_lydian_name),
            2, 4, 6, 7, 9, 11
        ),
        Scale.Type(
            getString(R.string.scale_mixolydian_name),
            2, 4, 5, 7, 9, 10
        ),
        Scale.Type(
            getString(R.string.scale_aeolian_name),
            2, 3, 5, 7, 8, 10
        ),
        Scale.Type(
            getString(R.string.scale_locrian_name),
            1, 3, 5, 6, 8, 10
        ),
        Scale.Type(
            getString(R.string.scale_harmonic_name),
            2, 3, 5, 7, 8, 11
        ),
        Scale.Type(
            getString(R.string.scale_melodic_name),
            2, 3, 5, 7, 9, 11
        ),
        Scale.Type(
            getString(R.string.scale_pentatonic_major_name),
            2, 4, 7, 9
        ),
        Scale.Type(
            getString(R.string.scale_pentatonic_minor_name),
            3, 5, 7, 10
        ),
        Scale.Type(
            getString(R.string.scale_pentatonic_blues_name),
            3, 5, 6, 7, 10
        )
    )
}

