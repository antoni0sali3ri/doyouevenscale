package com.github.antoni0sali3ri.doyouevenscale.core.model

import com.github.antoni0sali3ri.doyouevenscale.core.model.entity.Instrument
import kotlin.math.pow

/**
 * A FretSpacing defines the distances between frets.
 *
 * The distances need to be scaled to only show the fretboard, not the entire scale length.
 * The spacings are also relative to the portion of the fretboard shown,
 * such that the zero fret is at 0.0 and the last fret of the range is at 1.0
 */
interface FretSpacing {
    /**
     * Return the relative position for a given fret.
     */
    fun getFretPosition(fretNum: Int, fretCount: Int): Double

    /**
     * Return the relative positions for all frets in the given range, including the zero fret
     *
     * The length of the returned list of positions is always fretCount + 1.
     *
     * @param fretCount number of frets in range
     */
    fun getFretPositions(fretCount: Int) = (0..fretCount).map {
        getFretPosition(it, fretCount)
    }
}

/**
 * Constant fret spacing, all frets are the same distance apart.
 */
object ConstantFretSpacing : FretSpacing {
    override fun getFretPosition(fretNum: Int, fretCount: Int): Double {
        return fretNum.toDouble() / fretCount
    }
}

/**
 * Space out the frets according to equal temperament.
 *
 * More realistic than ConstantFretSpacing but may look very cramped with too many frets.
 */
object EqualTemperamentFretSpacing : FretSpacing {

    override fun getFretPosition(fretNum: Int, fretCount: Int): Double {
        val lastFretPosition = spacings[fretCount - 1]
        return spacings[fretNum - 1] / lastFretPosition
    }

    override fun getFretPositions(fretCount: Int): List<Double> {
        val spacingsForInstrument = spacings.subList(0, fretCount + 1)
        val lastFretPosition = spacingsForInstrument.last()
        return spacingsForInstrument.map {
            it / lastFretPosition
        }
    }

    private fun fretSpacing(n: Int) = 1.0 - (1.0 / 2.0.pow(n / 12.0))

    private val spacings by lazy {
        (0..Instrument.MaxFrets).map { fretSpacing(it) }
    }
}