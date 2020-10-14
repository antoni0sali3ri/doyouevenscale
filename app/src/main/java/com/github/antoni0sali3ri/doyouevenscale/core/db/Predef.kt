package com.github.antoni0sali3ri.doyouevenscale.core.db

import android.content.Context
import androidx.annotation.StringRes
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.Note
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.InstrumentPreset
import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Scale

/**
 * Predefined presets, instruments, tunings and scales.
 *
 * These values will be written to the database on the first run of the app.
 */
class Predef(private val context: Context) {

    fun getString(@StringRes res: Int): String = context.resources.getString(res)

    val configs = listOf(
        InstrumentPreset(1, 1, 1, Note.C, 0, 12).apply {
            showAsTab = 0
            name = getString(R.string.instrument_guitar_name)
        },
        InstrumentPreset(2, 9, 1, Note.C, 0, 12).apply {
            showAsTab = 1
            name = getString(R.string.instrument_bass_4str_name)
        },
        InstrumentPreset(5, 16, 1, Note.C, 0, 12).apply {
            name = getString(R.string.instrument_mandolin_name)
        },
        InstrumentPreset(7, 19, 1, Note.C, 0, 12).apply {
            name = getString(R.string.instrument_banjo_5str_name)
        },
        InstrumentPreset(8, 24, 1, Note.C, 0, 12).apply {
            name = getString(R.string.instrument_ukulele_name)
        }
    )

    val instruments = listOf(
        Instrument(6, getString(R.string.instrument_guitar_name)).apply { id = 1 },
        Instrument(4, getString(R.string.instrument_bass_4str_name)).apply { id = 2 },
        Instrument(5, getString(R.string.instrument_bass_5str_name)).apply { id = 3 },
        Instrument(6, getString(R.string.instrument_bass_6str_name)).apply { id = 4 },
        Instrument(4, getString(R.string.instrument_mandolin_name)).apply { id = 5 },
        Instrument(4, getString(R.string.instrument_banjo_4str_name)).apply { id = 6 },
        Instrument(5, getString(R.string.instrument_banjo_5str_name)).apply { id = 7 },
        Instrument(4, getString(R.string.instrument_ukulele_name)).apply { id = 8 }
    )

    val tunings = listOf(
        // Guitar tunings
        Instrument.Tuning(
            1,
            listOf(Note.E, Note.A, Note.D, Note.G, Note.B, Note.E),
            getString(R.string.tuning_standard_name)
        ).apply { id = 1 },
        Instrument.Tuning(
            1,
            listOf(Note.D, Note.A, Note.D, Note.G, Note.B, Note.E),
            getString(R.string.tuning_dropd_name)
        ).apply { id = 2 },
        Instrument.Tuning(
            1,
            listOf(Note.Dis, Note.Gis, Note.Cis, Note.Fis, Note.Ais, Note.Dis),
            getString(R.string.tuning_halfdown_name)
        ).apply { id = 3 },
        Instrument.Tuning(
            1,
            listOf(Note.D, Note.G, Note.C, Note.F, Note.A, Note.D),
            getString(R.string.tuning_fulldown_name)
        ).apply { id = 4 },
        Instrument.Tuning(
            1,
            listOf(Note.C, Note.G, Note.C, Note.F, Note.A, Note.D),
            getString(R.string.tuning_dropc_name)
        ).apply { id = 5 },
        Instrument.Tuning(
            1,
            listOf(Note.D, Note.A, Note.D, Note.Fis, Note.A, Note.D),
            getString(R.string.tuning_opend_name)
        ).apply { id = 6 },
        Instrument.Tuning(
            1,
            listOf(Note.D, Note.G, Note.D, Note.G, Note.B, Note.D),
            getString(R.string.tuning_openg_name)
        ).apply { id = 7 },
        Instrument.Tuning(
            1,
            listOf(Note.C, Note.G, Note.C, Note.G, Note.C, Note.E),
            getString(R.string.tuning_openc_name)
        ).apply { id = 8 },

        // Bass tunings
        Instrument.Tuning(
            2,
            listOf(Note.E, Note.A, Note.D, Note.G),
            getString(R.string.tuning_standard_name)
        ).apply { id = 9 },
        Instrument.Tuning(
            2,
            listOf(Note.D, Note.A, Note.D, Note.G),
            getString(R.string.tuning_dropd_name)
        ).apply { id = 10 },
        Instrument.Tuning(
            2,
            listOf(Note.Dis, Note.Gis, Note.Cis, Note.Fis),
            getString(R.string.tuning_halfdown_name)
        ).apply { id = 11 },
        Instrument.Tuning(
            2,
            listOf(Note.D, Note.G, Note.C, Note.F),
            getString(R.string.tuning_fulldown_name)
        ).apply { id = 12 },
        Instrument.Tuning(
            2,
            listOf(Note.C, Note.G, Note.C, Note.F),
            getString(R.string.tuning_dropc_name)
        ).apply { id = 13 },
        Instrument.Tuning(
            3,
            listOf(Note.B, Note.E, Note.A, Note.D, Note.G),
            getString(R.string.tuning_standard_name)
        ).apply { id = 14 },
        Instrument.Tuning(
            4,
            listOf(Note.B, Note.E, Note.A, Note.D, Note.G, Note.C),
            getString(R.string.tuning_standard_name)
        ).apply { id = 15 },

        // Mandolin tunings
        Instrument.Tuning(
            5,
            listOf(Note.G, Note.D, Note.A, Note.E),
            getString(R.string.tuning_standard_name)
        ).apply { id = 16 },
        Instrument.Tuning(
            5,
            listOf(Note.C, Note.G, Note.D, Note.A),
            getString(R.string.tuning_viola_name)
        ).apply { id = 17 },

        // Banjo tunings
        Instrument.Tuning(
            6,
            listOf(Note.C, Note.G, Note.D, Note.A),
            getString(R.string.tuning_standard_name)
        ).apply { id = 18 },
        Instrument.Tuning(
            7,
            listOf(Note.G, Note.D, Note.G, Note.B, Note.D),
            getString(R.string.tuning_openg_name)
        ).apply { id = 19 },
        Instrument.Tuning(
            7,
            listOf(Note.G, Note.D, Note.G, Note.Ais, Note.D),
            getString(R.string.tuning_opengm_name)
        ).apply { id = 20 },
        Instrument.Tuning(
            7,
            listOf(Note.G, Note.D, Note.G, Note.C, Note.D),
            getString(R.string.tuning_banjo_sawmill_name)
        ).apply { id = 21 },
        Instrument.Tuning(
            7,
            listOf(Note.G, Note.C, Note.G, Note.C, Note.D),
            getString(R.string.tuning_doublec_name)
        ).apply { id = 22 },
        Instrument.Tuning(
            7,
            listOf(Note.G, Note.C, Note.G, Note.C, Note.E),
            getString(R.string.tuning_openc_name)
        ).apply { id = 23 },

        // Ukulele tunings
        Instrument.Tuning(
            8,
            listOf(Note.G, Note.C, Note.E, Note.A),
            getString(R.string.tuning_ukulele_soprano_name)
        ).apply { id = 24 },
        Instrument.Tuning(
            8,
            listOf(Note.D, Note.G, Note.B, Note.E),
            getString(R.string.tuning_ukulele_baritone_name)
        ).apply { id = 25 }
    )

    val scaleTypes = listOf(
        Scale.Type(
            getString(R.string.scale_ionian_major_name),
            2, 4, 5, 7, 9, 11
        ).apply { id = 1 },
        Scale.Type(
            getString(R.string.scale_dorian_minor_name),
            2, 3, 5, 7, 9, 10
        ).apply { id = 2 },
        Scale.Type(
            getString(R.string.scale_phrygian_minor_name),
            1, 3, 5, 7, 8, 10
        ).apply { id = 3 },
        Scale.Type(
            getString(R.string.scale_lydian_major_name),
            2, 4, 6, 7, 9, 11
        ).apply { id = 4 },
        Scale.Type(
            getString(R.string.scale_mixolydian_major_name),
            2, 4, 5, 7, 9, 10
        ).apply { id = 5 },
        Scale.Type(
            getString(R.string.scale_aeolian_minor_name),
            2, 3, 5, 7, 8, 10
        ).apply { id = 6 },
        Scale.Type(
            getString(R.string.scale_locrian_minor_name),
            1, 3, 5, 6, 8, 10
        ).apply { id = 7 },
        Scale.Type(
            getString(R.string.scale_harmonic_minor_name),
            2, 3, 5, 7, 8, 11
        ).apply { id = 8 },
        Scale.Type(
            getString(R.string.scale_melodic_minor_name),
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
        ).apply { id = 12 },
        Scale.Type(
            getString(R.string.scale_whole_note_name),
            2, 4, 6, 8, 10
        ).apply { id = 13 }
    )
}

